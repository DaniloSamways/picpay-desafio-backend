package br.com.danilosamways.picpay_desafio_backend.transaction;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.danilosamways.picpay_desafio_backend.authorization.AuthorizerService;
import br.com.danilosamways.picpay_desafio_backend.wallet.Wallet;
import br.com.danilosamways.picpay_desafio_backend.wallet.WalletRepository;
import br.com.danilosamways.picpay_desafio_backend.wallet.WalletType;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final AuthorizerService authorizerService;

    public TransactionService(
            TransactionRepository transactionRepository,
            WalletRepository walletRepository,
            AuthorizerService authorizerService) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
        this.authorizerService = authorizerService;
    }

    @Transactional
    public Transaction create(Transaction transaction) {
        // 1 - Validação
        validate(transaction);

        // 2 - Criar a carteira
        var newTransaction = transactionRepository.save(transaction);

        // 3 - Debitar e Creditar na carteira
        var payerWallet = walletRepository.findById(transaction.payer()).get();
        var payeeWallet = walletRepository.findById(transaction.payee()).get();
        walletRepository.save(payerWallet.debit(transaction.value()));
        walletRepository.save(payeeWallet.credit(transaction.value()));

        // 4 - Chamar serviços externos
        authorizerService.authorize(transaction);

        return newTransaction;
    }

    /*
     * - the payer has a common wallet
     * - the payer has enough balance
     * - the payer is not the payee
     */
    private void validate(Transaction transaction) {
        walletRepository.findById(transaction.payee())
                .map(payee -> walletRepository.findById(transaction.payer())
                        .map(payer -> isTransactionValid(transaction, payer) ? transaction : null)
                        .orElseThrow(
                                () -> new InvalidTransactionException(
                                        "Invalid transaction - %s".formatted(transaction))))
                .orElseThrow(() -> new InvalidTransactionException(
                        "Invalid transaction - %s".formatted(transaction)));
    }

    private boolean isTransactionValid(Transaction transaction, Wallet payer) {
        return payer.type() == WalletType.COMUM.getValue() &&
                payer.balance().compareTo(transaction.value()) >= 0 &&
                !transaction.payer().equals(transaction.payee());
    }
}
