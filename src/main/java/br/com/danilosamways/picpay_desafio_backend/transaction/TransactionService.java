package br.com.danilosamways.picpay_desafio_backend.transaction;

import org.springframework.stereotype.Service;

import br.com.danilosamways.picpay_desafio_backend.wallet.WalletRepository;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;

    public TransactionService(TransactionRepository transactionRepository, WalletRepository walletRepository) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
    }

    public Transaction create(Transaction transaction) {
        // 1 - Validação

        // 2 - Criar a carteira
        var newTransaction = transactionRepository.save(transaction);

        // 3 - Debitar e Creditar na carteira
        var payerWallet = walletRepository.findById(transaction.payer()).get();
        var payeeWallet = walletRepository.findById(transaction.payee()).get();
        walletRepository.save(payerWallet.debit(transaction.value()));
        walletRepository.save(payeeWallet.credit(transaction.value()));

        // 4 - Chamar serviços externos

        return newTransaction;
    }
}
