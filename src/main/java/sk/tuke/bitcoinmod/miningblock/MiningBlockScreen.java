package sk.tuke.bitcoinmod.miningblock;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.text.ITextComponent;
import sk.tuke.bitcoinmod.EntryPoint;
import sk.tuke.bitcoinmod.transactionscapability.TransactionsCapability;
import sk.tuke.bitcoinmod.transactionscapability.TransactionsCapabilityProvider;
import sk.tuke.bitcoinmod.walletitem.WalletItem;

import java.awt.*;

public class MiningBlockScreen extends ContainerScreen<MiningBlockContainer> {
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(EntryPoint.MODID, "textures/gui/mining_block_gui.png");
    private TransactionsCapability transactionsCapability;
    private MiningBlockContainer miningBlockContainer;
    private static int WALLET_TEXT_X_POS = 29;
    private static int WALLET_TEXT_Y_POS = 17;
    private static int DIAMONDS_TEXT_X_POS = 110;
    private static int DIAMONDS_TEXT_Y_POS = 17;
    private static int BITCOIN_AMOUNT_TEXT_X_POS = WALLET_TEXT_X_POS - 15;
    private static int BITCOIN_AMOUNT_TEXT_Y_POS = WALLET_TEXT_Y_POS + 40;

    public MiningBlockScreen(MiningBlockContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.xSize = 175;
        this.ySize = 165;
        transactionsCapability = inv.player.world.getCapability(TransactionsCapabilityProvider.CAPABILITY_TRANSACTIONS, null).orElse(null);
        miningBlockContainer = screenContainer;
    }

    private float getBitcoinAmount(){
        Tuple<Long, Long> pair = WalletItem.getKeyPairFromWalletItemStack(miningBlockContainer.getWalletItemStack());
        return transactionsCapability.getTransactionsSum(pair.getB());
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.minecraft.getTextureManager().bindTexture(BACKGROUND_TEXTURE);
        int edgeSpacingX = (this.width - this.xSize) / 2;
        int edgeSpacingY = (this.height - this.ySize) / 2;
        this.blit(edgeSpacingX, edgeSpacingY, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        FontRenderer fontRenderer = this.minecraft.fontRenderer;
        fontRenderer.drawString("Wallet", WALLET_TEXT_X_POS, WALLET_TEXT_Y_POS, Color.BLACK.getRGB());
        fontRenderer.drawString("Diamonds", DIAMONDS_TEXT_X_POS, DIAMONDS_TEXT_Y_POS, Color.BLACK.getRGB());


        fontRenderer.drawString("BTC: " + getBitcoinAmount(), BITCOIN_AMOUNT_TEXT_X_POS, BITCOIN_AMOUNT_TEXT_Y_POS, Color.BLACK.getRGB());
    }

    @Override
    public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
        super.render(p_render_1_, p_render_2_, p_render_3_);
        this.renderHoveredToolTip(p_render_1_, p_render_2_);
    }
}
