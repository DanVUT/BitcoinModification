package sk.tuke.bitcoinmod.listwidget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.list.ExtendedList;
import sk.tuke.bitcoinmod.transactionscapability.model.Transaction;

import java.awt.*;
import java.util.Collection;
import java.util.Map;

/**
 * Implementuje listove zobrazenie pre Blockchain Block
 */
public class ListWidget extends ExtendedList<ListWidget.ListWidgetEntry> {
    Map<Integer, Transaction> transactions;

    /**
     * @param mcIn instancia minecraftu
     * @param transactions referencia na transakcie z TransactionCapability
     * @param widthIn sirka widgetu
     * @param heightIn vyska widgetu
     * @param topIn zaciatok listu zvrchu
     * @param bottomIn koniec listu odspodu
     * @param itemHeightIn vyska zaznamu
     */
    public ListWidget(Minecraft mcIn, Map<Integer, Transaction> transactions, int widthIn, int heightIn, int topIn, int bottomIn, int itemHeightIn) {
        super(mcIn, widthIn, heightIn, topIn, bottomIn, itemHeightIn);
        this.transactions = transactions;
    }

    /**
     * Sluzi k posunu zaznamov o 10 pixelov doprava aby neboli tak natlacene na okraji obrazovky
     * @return vracia hodnotu laveho okraja + 10 pixelov
     */
    @Override
    protected int getRowLeft() {
        return getLeft() + 10;
    }

    /**
     * Sluzi k posunu scroll baru aby sa nedotykal praveho okraja obrazovky
     * @return Vrati hodnotu praveho okraja - 7 pixelov
     */
    @Override
    protected int getScrollbarPosition() {
        return getRight() - 7;
    }

    /**
     * Naplni ListWidget zaznamami obsahujucimi informacie o transakciach
     * @param p_renderList_1_ nepouzity argument
     * @param p_renderList_2_ nepouzity argument
     * @param p_renderList_3_ nepouzity argument
     * @param p_renderList_4_ nepouzity argument
     * @param p_renderList_5_ nepouzity argument
     */
    @Override
    protected void renderList(int p_renderList_1_, int p_renderList_2_, int p_renderList_3_, int p_renderList_4_, float p_renderList_5_) {
        if(this.children().size() < this.transactions.size()){
            Collection<Transaction> transactionsValues = this.transactions.values();
            for(int i = this.children().size(); i < transactionsValues.size(); i++){
                this.children().add(new ListWidgetEntry(transactions.get(i)));
            }
        }
        super.renderList(p_renderList_1_, p_renderList_2_, p_renderList_3_, p_renderList_4_, p_renderList_5_);
    }

    /**
     * Trieda implementujuca zaznam v ListWidgete
     */
    public static class ListWidgetEntry extends ExtendedList.AbstractListEntry<ListWidgetEntry> {
        private Transaction transaction;

        /**
         * @param transaction transakcia, ktoru ma instancia zaznamu vykreslit
         */
        public ListWidgetEntry(Transaction transaction){
            this.transaction = transaction;
        }

        /**
         * Metoda definuje ako sa ma vykreslit zaznam v liste transakcii
         * @param index index zaznamu
         * @param rowTop zaciatok zaznamu zvrchu
         * @param rowLeft zaciatok zaznamu zlava
         * @param rowWidth sirka zaznamu
         * @param rowHeight vyska zaznamu
         * @param mouseX X pozicia mysi
         * @param mouseY Y pozicia mysi
         * @param isMouseOverThisEntry ukazuje mys na zaznam
         * @param p_render_9_ nepouzity argument
         */
        @Override
        public void render(int index, int rowTop, int rowLeft, int rowWidth, int rowHeight, int mouseX, int mouseY, boolean isMouseOverThisEntry , float p_render_9_) {
            FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;
            int yPos = rowTop;
            fontRenderer.drawString("Transaction ID: " + transaction.getTransactionID(), rowLeft, yPos, Color.WHITE.getRGB());
            yPos += 10;
            if(transaction.isBaseTransaction()){
                fontRenderer.drawString("Coinbase Transaction", rowLeft, yPos, Color.GREEN.getRGB());
            } else {
                fontRenderer.drawString("Sender: " + Long.toHexString(transaction.getSenderBitcoinAddress()), rowLeft, yPos, Color.YELLOW.getRGB());
            }
            yPos += 15;

            long recipientPublicKey = transaction.getTransactionOutputs().get(0).getRecipientBitcoinAddress();
            fontRenderer.drawString("Recipient: " + Long.toHexString(recipientPublicKey), rowLeft, yPos, Color.WHITE.getRGB());
            yPos += 10;
            fontRenderer.drawString("Amount: " + transaction.getTransactionOutputs().get(0).getBitcoinAmount(), rowLeft, yPos, Color.WHITE.getRGB());
            fontRenderer.drawString(transaction.getTransactionOutputs().get(0).isSpent() ? "Spent" : "Available", rowLeft + 110, yPos, transaction.getTransactionOutputs().get(0).isSpent() ? Color.RED.getRGB() : Color.green.getRGB());
            yPos += 20;
            if(transaction.getTransactionOutputs().size() == 2){
                long changePublicKey = transaction.getTransactionOutputs().get(1).getRecipientBitcoinAddress();
                fontRenderer.drawString("Change: " + Long.toHexString(changePublicKey), rowLeft, yPos, Color.WHITE.getRGB());
                yPos += 10;
                fontRenderer.drawString("Amount: " + transaction.getTransactionOutputs().get(1).getBitcoinAmount(), rowLeft, yPos, Color.WHITE.getRGB());
                fontRenderer.drawString(transaction.getTransactionOutputs().get(1).isSpent() ? "Spent" : "Available", rowLeft + 110, yPos, transaction.getTransactionOutputs().get(1).isSpent() ? Color.RED.getRGB() : Color.green.getRGB());
            }

            int linePos = rowTop + rowHeight - 10;
            fontRenderer.drawString("________________________________________________", rowLeft, linePos, Color.WHITE.getRGB());

        }
    }
}
