package sk.tuke.bitcoinmod.listwidget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.list.ExtendedList;
import sk.tuke.bitcoinmod.transactionscapability.model.Transaction;

import java.awt.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ListWidget extends ExtendedList<ListWidget.ListWidgetEntry> {
    Map<Integer, Transaction> transactions;
    public ListWidget(Minecraft mcIn, Map<Integer, Transaction> transactions, int widthIn, int heightIn, int topIn, int bottomIn, int itemHeightIn) {
        super(mcIn, widthIn, heightIn, topIn, bottomIn, itemHeightIn);
        this.transactions = transactions;
    }

    @Override
    protected int getRowLeft() {
        return getLeft() + 10;
    }

    @Override
    protected int getScrollbarPosition() {
        return getRight() - 7;
    }

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

    public static class ListWidgetEntry extends ExtendedList.AbstractListEntry<ListWidgetEntry> {
        private Transaction transaction;
        public ListWidgetEntry(Transaction transaction){
            this.transaction = transaction;
        }

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
