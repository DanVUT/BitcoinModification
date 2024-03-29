package sk.tuke.bitcoinmod.transactionscapability;

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
    /**
     * Mapa transakcii
     */
    Map<Integer, Transaction> transactions;
    /**
     * Celkovy pocet Bitcoinov v obehu
     */
    private float bitcoinsTotal;
    /**
     * Prah ktory sa bude pripocitavat po tom, co bitcoinsTotal sa bude rovnat nextThreshold
     */
    private float threshold;
    /**
     * Stav ked sa celkovy pocet Bitcoinov bude rovnat tejto hodnote, tak sa bude menit baseAmount
     */
    private float nextThreshold;
    /**
     * Pripocitavana suma Bitcoinov v ramci Coinbase transakcii
     */
    private float baseAmount;

    public TransactionsCapability(){
        transactions = new HashMap<>();
        threshold = 10500000.0f;
        nextThreshold = threshold;
        bitcoinsTotal = 0.0f;
        baseAmount = 50.0f;
    }

    /**
     * Metoda vrati vsetky neminute transakcie danej Bitcoin adresy
     * @param bitcoinAddress Bitcoin adresa
     * @return Vsetky neminute transakcie danej Bitcoin adresy
     */
    //Gets all transactions which have unspent transaction output with given bitcoin address
    public List<Integer> getTransactions(long bitcoinAddress){
        Map<Integer, Transaction> tmp =  transactions
                .entrySet()
                .stream()
                .filter(m -> {
                    for(TransactionOutput to : m.getValue().getTransactionOutputs()){
                        if(to.getRecipientBitcoinAddress() == bitcoinAddress && !to.isSpent()){
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

    /**
     * @param transactionsIDs list ID transakcii
     * @param bitcoinAddress bitcoin adresa
     * @return vrati sucet Bitcoinov z poskytnutych ID pre danu Bitcoin adresu
     */
    //Gets sum from all given transactions for given bitcoin address
    public float getTransactionsSum(List<Integer> transactionsIDs, long bitcoinAddress){
        float sum = 0.0f;
        for(int id : transactionsIDs){
            Transaction transaction = transactions.get(id);

            for(TransactionOutput to : transaction.getTransactionOutputs()){
                if(to.getRecipientBitcoinAddress() == bitcoinAddress && !to.isSpent()){
                    sum += to.getBitcoinAmount();
                }
            }
        }
        return sum;
    }

    /**
     * Metoda vrati sucet Bitcoinov pre zadanu Bitcoin adresu
     * @param bitcoinAddress Bitcoin adresa
     * @return sucet Bitcoinov pre danu Bitcoin adresu
     */
    //Gets sum from all transactions for given bitcoin address
    public float getTransactionsSum(long bitcoinAddress){
        return getTransactionsSum(getTransactions(bitcoinAddress), bitcoinAddress);
    }

    /**
     * Metoda prida transakciu do mnoziny vsetkych transakcii
     * @param transaction pridavana transakcia
     */
    //Adds transaction into transactions pool
    public void addTransaction(Transaction transaction){
        transactions.put(transaction.getTransactionID(), transaction);
    }

    /**
     * Metoda namapuje list transakcii na mapu transakcii
     * @param transactions list transakcii
     */
    //Maps transactions from list into transactions map
    public void mapTransactions(List<Transaction> transactions){
        this.transactions.clear();
        for(Transaction transaction : transactions){
            this.transactions.put(transaction.getTransactionID(), transaction);
        }
    }

    /**
     * Oznaci transakcne vystupy transakcii v liste ako minute
     * @param usedTransactions List pouzitych transakcii a ich vystupov
     */
    //Marks transactions as spent
    public void markTransactionsAsSpent(List<Tuple<Integer, Integer>> usedTransactions){
        for(Tuple<Integer, Integer> entry : usedTransactions){
            this.transactions.get(entry.getA()).getTransactionOutputs().get(entry.getB()).setSpent(true);
        }
    }

    /**
     * @return vrati mapu transakcii
     */
    //Returns map of transactions
    public Map<Integer, Transaction> getAllTransactions(){
        return transactions;
    }

    /**
     * Vytvori Coinbase transakciu
     * @param recipientBitcoinAddress Bitcoin adresa ktora "vytazila" Bitcoiny
     * @return vytvorena Coinbase transakcia
     */
    //Creates base transaction for given bitcoin address
    public Transaction createBaseTransaction(long recipientBitcoinAddress){
        Transaction newTransaction = new Transaction(transactions.size(), 0, recipientBitcoinAddress, true);
        newTransaction.setTransactionOutput(new TransactionOutput(recipientBitcoinAddress, baseAmount, false));
        addTransaction(newTransaction);

        bitcoinsTotal += baseAmount;
        if(bitcoinsTotal > nextThreshold){
            threshold /= 2;
            nextThreshold += threshold;
            baseAmount /= 2;
        }
        return newTransaction;
    }

    /**
     * Metoda vytvori novu pouzivatelsku transakciu a oznaci pouzite Transakcie ako minute
     * @param senderBitcoinAddress Bitcoin adresa odosielatela
     * @param recipientBitcoinAddress Bitcoin adres prijimatela
     * @param bitcoinAmount Mnozstvo posielanych Bitcoinov
     * @return Transakcia a List pouzitych transakcii
     * @throws RuntimeException V pripade, ze je niektory z udajov nezmyselny
     */
    //Creates new transaction from sender to recipient
    public Tuple<Transaction, List<Tuple<Integer, Integer>>> createNewTransaction(long senderBitcoinAddress, long recipientBitcoinAddress, float bitcoinAmount) throws RuntimeException{
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
        addTransaction(newTransaction);
        return new Tuple<>(newTransaction, usedTransactions);
    }


    /**
     * Trieda sluzi na serializaciu/deserializaciu TransactionsCapability
     */
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

        /**
         * Metoda serializuje TransactionsCapability do NBT struktury
         * @param capability nepouzity argument
         * @param instance instancia TransactionsCapability
         * @param side nevyuzity argument
         * @return serializovana TransactionsCapability do NBT struktury
         */
        @Nullable
        @Override
        public INBT writeNBT(Capability<TransactionsCapability> capability, TransactionsCapability instance, Direction side) {
            CompoundNBT nbt = new CompoundNBT();
            //General data
            nbt.putInt(TRANSACTIONS_COUNT_TAG, instance.transactions.size());
            nbt.putFloat(BITCOINS_TOTAL_TAG, instance.bitcoinsTotal);
            nbt.putFloat(THRESHOLD_TAG, instance.threshold);
            nbt.putFloat(NEXT_THRESHOLD_TAG, instance.nextThreshold);
            nbt.putFloat(BASE_AMOUNT_TAG, instance.baseAmount);
            int transactionIndex = 0;
            //Writing transactions
            for(Transaction transaction : instance.transactions.values()){
                CompoundNBT transactionNbt = new CompoundNBT();

                //Transaction general data
                transactionNbt.putInt(TRANSACTION_ID_TAG, transaction.getTransactionID());
                transactionNbt.putLong(TRANSACTION_SENDER_ADDRESS, transaction.getSenderBitcoinAddress());
                transactionNbt.putLong(TRANSACTION_RECIPIENT_ADDRESS, transaction.getRecipientBitcoinAddress());
                transactionNbt.putBoolean(TRANSACTION_IS_BASE_TAG, transaction.isBaseTransaction());
                transactionNbt.putInt(TRANSACTION_OUTPUTS_COUNT_TAG, transaction.getTransactionOutputs().size());

                //Writing transaction outputs
                int toIndex = 0;
                for(TransactionOutput to : transaction.getTransactionOutputs()){
                    CompoundNBT outputNbt = new CompoundNBT();
                    //TransactionOutput details
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

        /**
         * Metoda deserializuje TransactionsCapability z NBT struktury
         * @param capability nevyuzity argument
         * @param instance instancia TransactionsCapability ktora sa bude naplnat z NBT
         * @param side nevyuzity argument
         * @param nbt NBT obsahujuce serializovanu TransactionsCapability
         */
        @Override
        public void readNBT(Capability<TransactionsCapability> capability, TransactionsCapability instance, Direction side, INBT nbt) {
            CompoundNBT parentNbt = (CompoundNBT) nbt;
            //Read general transactions data details
            int transactionsCount = parentNbt.getInt(TRANSACTIONS_COUNT_TAG);
            instance.bitcoinsTotal = parentNbt.getFloat(BITCOINS_TOTAL_TAG);
            instance.threshold = parentNbt.getFloat(THRESHOLD_TAG);
            instance.nextThreshold = parentNbt.getFloat(NEXT_THRESHOLD_TAG);
            instance.baseAmount = parentNbt.getFloat(BASE_AMOUNT_TAG);

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
