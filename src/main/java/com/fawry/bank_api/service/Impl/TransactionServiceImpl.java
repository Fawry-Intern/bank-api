package com.fawry.bank_api.service.Impl;

import com.fawry.bank_api.dto.transaction.DepositRequest;
import com.fawry.bank_api.dto.transaction.TransactionDetailsResponse;
import com.fawry.bank_api.dto.transaction.WithdrawRequest;
import com.fawry.bank_api.entity.Account;
import com.fawry.bank_api.entity.Transaction;
import com.fawry.bank_api.enums.TransactionType;
import com.fawry.bank_api.exception.EntityNotFoundException;
import com.fawry.bank_api.exception.IllegalActionException;
import com.fawry.bank_api.exception.InsufficientFundsException;
import com.fawry.bank_api.mapper.TransactionMapper;
import com.fawry.bank_api.repository.AccountRepository;
import com.fawry.bank_api.repository.TransactionRepository;
import com.fawry.bank_api.service.TransactionService;
import com.fawry.kafka.dtos.PaymentDetails;
import com.fawry.kafka.dtos.PaymentMethod;
import com.fawry.kafka.events.OrderCanceledEventDTO;
import com.fawry.kafka.events.PaymentCreatedEventDTO;
import com.fawry.kafka.events.StoreCreatedEventDTO;
import com.fawry.kafka.producers.PaymentCancellationPublisher;
import com.fawry.kafka.producers.PaymentCreatedPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class TransactionServiceImpl implements TransactionService {

    private static final Logger log = LoggerFactory.getLogger(TransactionServiceImpl.class);
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionMapper transactionMapper;
    private final PaymentCancellationPublisher paymentCancellationPublisher;
    private final PaymentCreatedPublisher paymentCreatedPublisher;

    @Value("${custom.merchant.card-number}")
    private String MERCHANT_CARD_NUMBER;

    @Value("${custom.merchant.cvv}")
    private String MERCHANT_CVV;

    @Autowired
    public TransactionServiceImpl(
            TransactionRepository transactionRepository,
            AccountRepository accountRepository,
            TransactionMapper transactionMapper, PaymentCancellationPublisher paymentCancellationPublisher, PaymentCreatedPublisher paymentCreatedPublisher) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.transactionMapper = transactionMapper;
        this.paymentCancellationPublisher = paymentCancellationPublisher;
        this.paymentCreatedPublisher = paymentCreatedPublisher;
    }

    @Override
    @Transactional
    @KafkaListener(topics = "store-updated-events", groupId = "bank_store_id")
    public void makePayment(StoreCreatedEventDTO orderRequest) {
        try{
            log.info("store consume successfully {}", orderRequest);
            PaymentMethod paymentMethod = orderRequest.getPaymentMethod();
            PaymentDetails paymentDetails = paymentMethod.details();
            String cardNumber = paymentDetails.getNumber();
            String cvv = paymentDetails.getCvv();
            Account account = accountRepository.findByCardNumberAndCvv(cardNumber, cvv)
                    .orElseThrow(() -> new EntityNotFoundException(String.format("Account not found with cardNumber {} and cvv {}", cardNumber, cvv)));

            WithdrawRequest withdrawRequest = WithdrawRequest
                    .builder()
                    .accountId(account.getId())
                    .amount(orderRequest.getPaymentAmount())
                    .note("payment order number " + orderRequest.getOrderId())
                    .build();

            Account merchant = accountRepository.findByCardNumberAndCvv(MERCHANT_CARD_NUMBER, MERCHANT_CVV)
                    .orElseThrow(() -> new EntityNotFoundException(String.format("Account not found with cardNumber {} and cvv {}", cardNumber, cvv)));
            DepositRequest depositRequest = DepositRequest.builder()
                    .accountId(merchant.getId())
                    .amount(orderRequest.getPaymentAmount())
                    .note("new place order " + orderRequest.getOrderId())
                    .build();

            withdraw(withdrawRequest);
            deposit(depositRequest);

            PaymentCreatedEventDTO paymentCreatedEventDTO =
                    PaymentCreatedEventDTO.builder()
                            .orderId(orderRequest.getOrderId())
                            .userId(orderRequest.getUserId())
                            .paymentAmount(orderRequest.getPaymentAmount())
                            .customerContact(orderRequest.getCustomerContact())
                            .customerEmail(orderRequest.getCustomerEmail())
                            .addressDetails(orderRequest.getAddressDetails())
                            .customerName(orderRequest.getCustomerName())
                            .build();
            paymentCreatedPublisher.publishPaymentUpdatedEvent(paymentCreatedEventDTO);
        }catch (Exception e) {
            OrderCanceledEventDTO orderCanceledEventDTO = new OrderCanceledEventDTO(orderRequest.getOrderId(), e.getMessage(), orderRequest.getCustomerEmail());
            paymentCancellationPublisher.publishOrderCanceledEvent(orderCanceledEventDTO);
            log.info("This fail will cancel order with id {}", orderRequest.getOrderId());
        }

    }

    @Override
    @Transactional
    public TransactionDetailsResponse deposit(DepositRequest depositRequest) {
        Account account = accountRepository.findById(depositRequest.accountId())
                .orElseThrow(() -> new EntityNotFoundException("Account not found with ID: " + depositRequest.accountId()));

        if (!account.getIsActive()) {
            throw new IllegalActionException("Cannot deposit to an inactive account");
        }

        BigDecimal newBalance = account.getBalance().add(depositRequest.amount());
        account.setBalance(newBalance);
        accountRepository.save(account);

        BigDecimal roundedAmount = depositRequest.amount().setScale(2, RoundingMode.HALF_UP);
        Transaction transaction = Transaction.builder()
                .account(account)
                .type(TransactionType.DEPOSIT)
                .note(depositRequest.note())
                .amount(roundedAmount)
                .build();

        transaction = transactionRepository.save(transaction);
        return transactionMapper.toTransactionResponse(transaction);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public TransactionDetailsResponse withdraw(WithdrawRequest withdrawRequest) {
        Account account = accountRepository.findById(withdrawRequest.accountId())
                .orElseThrow(() -> new EntityNotFoundException("Account not found with ID: " + withdrawRequest.accountId()));

        if (!account.getIsActive()) {
            throw new IllegalActionException("Cannot withdraw from an inactive account");
        }

        if (account.getBalance().compareTo(withdrawRequest.amount()) < 0) {
            throw new InsufficientFundsException("Insufficient funds for withdrawal");
        }

        BigDecimal newBalance = account.getBalance().subtract(withdrawRequest.amount());
        account.setBalance(newBalance);
        accountRepository.save(account);

        BigDecimal roundedAmount = withdrawRequest.amount().setScale(2, RoundingMode.HALF_UP);
        Transaction transaction = Transaction.builder()
                .account(account)
                .type(TransactionType.WITHDRAW)
                .note(withdrawRequest.note())
                .amount(roundedAmount)
                .build();

        Transaction saveTransaction = transactionRepository.save(transaction);
        return transactionMapper.toTransactionResponse(transaction);
    }

}