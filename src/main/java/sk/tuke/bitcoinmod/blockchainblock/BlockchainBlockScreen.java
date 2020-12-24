package sk.tuke.bitcoinmod.blockchainblock;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import sk.tuke.bitcoinmod.listwidget.ListWidget;
import sk.tuke.bitcoinmod.transactionscapability.TransactionsCapability;
import sk.tuke.bitcoinmod.transactionscapability.TransactionsCapabilityProvider;

import java.util.Optional;

public class BlockchainBlockScreen extends Screen {
    private ListWidget listWidget;
    private TransactionsCapability transactionsCapability;

    protected BlockchainBlockScreen(ITextComponent titleIn) {
        super(titleIn);

        Optional<ClientWorld> clientWorld = LogicalSidedProvider.CLIENTWORLD.get(LogicalSide.CLIENT);
        transactionsCapability = clientWorld.get().getCapability(TransactionsCapabilityProvider.CAPABILITY_TRANSACTIONS, null).orElse(null);
    }

    @Override
    protected void init() {
        super.init();
        this.listWidget = new ListWidget(this.minecraft, transactionsCapability.getAllTransactions(), this.width, this.height, 32, this.height - 32, 70);
        this.children.add(listWidget);
    }

    @Override
    public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
        super.render(p_render_1_, p_render_2_, p_render_3_);
        this.listWidget.render(p_render_1_, p_render_2_, p_render_3_);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
