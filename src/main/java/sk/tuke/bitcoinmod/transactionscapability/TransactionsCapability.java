package sk.tuke.bitcoinmod.transactionscapability;

import com.google.common.collect.Collections2;
import javafx.util.Pair;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.Tuple;
import net.minecraftforge.common.capabilities.Capability;
import sk.tuke.bitcoinmod.transactionscapability.model.Transaction;
import sk.tuke.bitcoinmod.transactionscapability.model.TransactionOutput;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class TransactionsCapability {
    Map<Integer, Transaction> transactions;
    private float bitcoinsTotal;
    private float threshold;
    private float nextThreshold;
    private float baseAmount;

    public TransactionsCapability(){
        transactions = new HashMap<>();
        threshold = 10500000;
        nextThreshold = threshold;
        bitcoinsTotal = 0;
        baseAmount = 50;
    }

    public List<Integer> getTransactions(long bitcoinAddress){
        Map<Integer, Transaction> tmp =  transactions
                .entrySet()
                .stream()
                .filter(m -> {
                    for(TransactionOutput to : m.getValue().getTransactionOutputs()){
                        if(to.getRecipientBitcoinAddress() == bitcoinAddress){
                            return true;
                        }
                    }
                    return false;
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        Integer[] keys = new Integer[0];
        keys = tmp.keySet().toArray(keys);
        Arrays.sort(keys);
        return Arrays.asList(keys);
    }

    public float getTransactionsSum(List<Integer> transactionsIDs, long bitcoinAddress){
        float sum = 0.0f;
        for(int id : transactionsIDs){
            Transaction transaction = transactions.get(id);

            for(TransactionOutput to : transaction.getTransactionOutputs()){
                if(to.getRecipientBitcoinAddress() == bitcoinAddress){
                    sum += to.getBitcoinAmount();
                }
            }
        }
        return sum;
    }

    public float getTransactionsSum(long bitcoinAddress){
        return getTransactionsSum(getTransactions(bitcoinAddress), bitcoinAddress);
    }

    public void addTransaction(Transaction transaction){
        transactions.put(transaction.getTransactionID(), transaction);
    }

    public void mapTransactions(List<Transaction> transactions){
        this.transactions.clear();
        for(Transaction transaction : transactions){
            this.transactions.put(transaction.getTransactionID(), transaction);
        }
    }

    public void markTransactionsAsSpent(List<Tuple<Integer, Integer>> usedTransactions){
        for(Tuple<Integer, Integer> entry : usedTransactions){
            this.transactions.get(entry.getA()).getTransactionOutputs().get(entry.getB()).setSpent(true);
        }
    }

    public Map<Integer, Transaction> getAllTransactions(){
        return transactions;
    }

    public Transaction createBaseTransaction(long recipientBitcoinAddress){
        Transaction newTransaction = new Transaction(transactions.size(), 0, recipientBitcoinAddress, true);
        newTransaction.setTransactionOutput(new TransactionOutput(recipientBitcoinAddress, baseAmount, false));
        bitcoinsTotal += baseAmount;
        if(bitcoinsTotal > nextThreshold){
            threshold /= 2;
            nextThreshold += threshold;
            baseAmount /= 2;
        }
        addTransaction(newTransaction);
        return newTransaction;
    }

    public Tuple<Transaction, List<Tuple<Integer, Integer>>> createNewTransaction(long senderBitcoinAddress, long recipientBitcoinAddress, float bitcoinAmount){
        List<Integer> transactionsIDs = getTransactions(senderBitcoinAddress);
        float totalBitcoinSum = getTransactionsSum(transactionsIDs, senderBitcoinAddress);

        if(totalBitcoinSum < bitcoinAmount){
            throw new RuntimeException();
        }

        List<Tuple<Integer, Integer>> usedTransactions = new ArrayList<>();
        float usedBitcoins = 0.0f;
        for(int id : transactionsIDs){
            Transaction transaction = transactions.get(id);

            for(TransactionOutput to : transaction.getTransactionOutputs()){
                if(to.getRecipientBitcoinAddress() == senderBitcoinAddress){
                    usedBitcoins += to.getBitcoinAmount();
                    to.setSpent(true);
                    usedTransactions.add(new Tuple<>(id, transaction.getTransactionOutputs().indexOf(to)));
                }
            }
            if(usedBitcoins >= bitcoinAmount){
                break;
            }
        }

        Transaction newTransaction = new Transaction(transactions.size(), senderBitcoinAddress, recipientBitcoinAddress, false);
        TransactionOutput transactionOutput = new TransactionOutput(recipientBitcoinAddress, bitcoinAmount, false);
        newTransaction.setTransactionOutput(transactionOutput);
        if(usedBitcoins > bitcoinAmount){
            TransactionOutput changeOutput = new TransactionOutput(senderBitcoinAddress, usedBitcoins - bitcoinAmount, true);
            newTransaction.setTransactionOutput(changeOutput);
        }

        return new Tuple<>(newTransaction, usedTransactions);
    }



    public static class TransactionsCapabilityStorage implements Capability.IStorage<TransactionsCapability>{
        private static final String TRANSACTIONS_COUNT_TAG = "TRANSACTIONS_COUNT";
        private static final String BITCOINS_TOTAL_TAG = "BITCOINS_TOTAL";
        private static final String THRESHOLD_TAG = "THRESHOLD";
        private static final String NEXT_THRESHOLD_TAG = "NEXT_THRESHOLD";
        private static final String BASE_AMOUNT_TAG = "BASE_AMOUNT";
        private static final String TRANSACTION_TAG = "TRANSACTION_";
        private static final String TRANSACTION_ID_TAG = "TRANSACTION_ID";
        private static final String TRANSACTION_SENDER_ADDRESS = "TRANSACTION_SENDER";
        private static final String TRANSACTION_RECIPIENT_ADDRESS = "TRANSACTION_RECIPIENT";
        private static final String TRANSACTION_IS_BASE_TAG = "TRANSACTION_IS_BASE";
        private static final String TRANSACTION_OUTPUTS_COUNT_TAG = "TRANSACTION_OUTPUTS_COUNT";
        private static final String TRANSACTION_OUTPUT_TAG = "TRANSACTION_OUTPUT_";
        private static final String TRANSACTION_OUTPUT_RECIPIENT_ADDRESS = "TRANSACTION_OUTPUT_RECIPIENT_ADDRESS";
        private static final String TRANSACTION_OUTPUT_BITCOIN_AMOUNT = "TRANSACTION_OUTPUT_BITCOIN_AMOUNT";
        private static final String TRANSACTION_OUTPUT_IS_CHANGE = "TRANSACTION_OUTPUT_IS_CHANGE";
        private static final String TRANSACTION_OUTPUT_IS_SPENT = "TRANSACTION_OUTPUT_IS_SPENT";

        @Nullable
        @Override
        public INBT writeNBT(Capability<TransactionsCapability> capability, TransactionsCapability instance, Direction side) {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putInt(TRANSACTIONS_COUNT_TAG, instance.transactions.size());
            nbt.putFloat(BITCOINS_TOTAL_TAG, instance.bitcoinsTotal);
            nbt.putFloat(THRESHOLD_TAG, instance.threshold);
            nbt.putFloat(NEXT_THRESHOLD_TAG, instance.nextThreshold);
            nbt.putFloat(BASE_AMOUNT_TAG, instance.baseAmount);
            int transactionIndex = 0;
            for(Transaction transaction : instance.transactions.values()){
                CompoundNBT transactionNbt = new CompoundNBT();

                transactionNbt.putInt(TRANSACTION_ID_TAG, transaction.getTransactionID());
                transactionNbt.putLong(TRANSACTION_SENDER_ADDRESS, transaction.getSenderBitcoinAddress());
                transactionNbt.putLong(TRANSACTION_RECIPIENT_ADDRESS, transaction.getRecipientBitcoinAddress());
                transactionNbt.putBoolean(TRANSACTION_IS_BASE_TAG, transaction.isBaseTransaction());
                transactionNbt.putInt(TRANSACTION_OUTPUTS_COUNT_TAG, transaction.getTransactionOutputs().size());

                int toIndex = 0;
                for(TransactionOutput to : transaction.getTransactionOutputs()){
                    CompoundNBT outputNbt = new CompoundNBT();
                    outputNbt.putLong(TRANSACTION_OUTPUT_RECIPIENT_ADDRESS, to.getRecipientBitcoinAddress());
                    outputNbt.putFloat(TRANSACTION_OUTPUT_BITCOIN_AMOUNT, to.getBitcoinAmount());
                    outputNbt.putBoolean(TRANSACTION_OUTPUT_IS_CHANGE, to.isChange());
                    outputNbt.putBoolean(TRANSACTION_OUTPUT_IS_SPENT, to.isSpent());

                    transactionNbt.put(TRANSACTION_OUTPUT_TAG + toIndex, outputNbt);
                    toIndex++;
                }
                nbt.put(TRANSACTION_TAG + transactionIndex, transactionNbt);
                transactionIndex++;
            }
            return nbt;
        }

        @Override
        public void readNBT(Capability<TransactionsCapability> capability, TransactionsCapability instance, Direction side, INBT nbt) {
            CompoundNBT parentNbt = (CompoundNBT) nbt;
            int transactionsCount = parentNbt.getInt(TRANSACTIONS_COUNT_TAG);
            instance.baseAmount = parentNbt.getFloat(BASE_AMOUNT_TAG);
            instance.nextThreshold = parentNbt.getFloat(NEXT_THRESHOLD_TAG);
            instance.threshold = parentNbt.getFloat(THRESHOLD_TAG);
            instance.bitcoinsTotal = parentNbt.getFloat(BITCOINS_TOTAL_TAG);

            for(int i = 0; i < transactionsCount; i++){
                CompoundNBT transactionNbt = parentNbt.getCompound(TRANSACTION_TAG + i);
                int transactionId = transactionNbt.getInt(TRANSACTION_ID_TAG);
                long senderAddress = transactionNbt.getLong(TRANSACTION_SENDER_ADDRESS);
                long recipientAddress = transactionNbt.getLong(TRANSACTION_RECIPIENT_ADDRESS);
                boolean isBase = transactionNbt.getBoolean(TRANSACTION_IS_BASE_TAG);

                Transaction transaction = new Transaction(transactionId, senderAddress, recipientAddress, isBase);

                int outputsCount = transactionNbt.getInt(TRANSACTION_OUTPUTS_COUNT_TAG);

                for(int j = 0; j < outputsCount; j++){
                    CompoundNBT outputNbt = transactionNbt.getCompound(TRANSACTION_OUTPUT_TAG + j);

                    long recipientAddressOutput = outputNbt.getLong(TRANSACTION_OUTPUT_RECIPIENT_ADDRESS);
                    float bitcoinsAmount = outputNbt.getFloat(TRANSACTION_OUTPUT_BITCOIN_AMOUNT);
                    boolean isChange = outputNbt.getBoolean(TRANSACTION_OUTPUT_IS_CHANGE);
                    boolean isSpent = outputNbt.getBoolean(TRANSACTION_OUTPUT_IS_SPENT);

                    transaction.setTransactionOutput(new TransactionOutput(recipientAddressOutput, bitcoinsAmount, isChange, isSpent));
                }

                instance.addTransaction(transaction);
            }
        }
    }
}
