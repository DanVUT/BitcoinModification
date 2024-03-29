package sk.tuke.bitcoinmod.miningblock;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sk.tuke.bitcoinmod.EntryPoint;
import sk.tuke.bitcoinmod.interfaces.IRefreshable;
import sk.tuke.bitcoinmod.transactionscapability.TransactionsCapability;
import sk.tuke.bitcoinmod.transactionscapability.TransactionsCapabilityProvider;
import sk.tuke.bitcoinmod.walletitem.WalletItem;
import java.awt.*;

/**
 * Trieda implementujuca GUI Mining Blocku.
 */
@OnlyIn(Dist.CLIENT)
public class MiningBlockScreen extends ContainerScreen<MiningBlockContainer> implements IRefreshable {
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(EntryPoint.MODID, "textures/gui/mining_block_gui.png");
    private TransactionsCapability transactionsCapability;
    private MiningBlockContainer miningBlockContainer;
    private float bitcoinAmount;
    private static int WALLET_TEXT_X_POS = 29;
    private static int WALLET_TEXT_Y_POS = 17;
    private static int DIAMONDS_TEXT_X_POS = 110;
    private static int DIAMONDS_TEXT_Y_POS = 17;
    private static int BITCOIN_AMOUNT_TEXT_X_POS = WALLET_TEXT_X_POS - 15;
    private static int BITCOIN_AMOUNT_TEXT_Y_POS = WALLET_TEXT_Y_POS + 40;

    /**
     * Konstruktor nastavi velkost okna na velkost aku ma textura Mining Block GUI (175x165)
     * @param screenContainer referencia MiningBlockContaineru
     * @param inv hracov inventar
     * @param titleIn nevyuzity argument
     */
    public MiningBlockScreen(MiningBlockContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.xSize = 175;
        this.ySize = 165;
        transactionsCapability = inv.player.world.getCapability(TransactionsCapabilityProvider.CAPABILITY_TRANSACTIONS, null).orElse(null);
        miningBlockContainer = screenContainer;
        bitcoinAmount = 0.0f;
    }


    /**
     * Metoda namapuje texturu Mining Block GUI s  Mining Block Screen
     * @param partialTicks nevyuzity argument
     * @param mouseX nevyuzity argument
     * @param mouseY nevyuzity argument
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.minecraft.getTextureManager().bindTexture(BACKGROUND_TEXTURE);
        int edgeSpacingX = (this.width - this.xSize) / 2;
        int edgeSpacingY = (this.height - this.ySize) / 2;
        this.blit(edgeSpacingX, edgeSpacingY, 0, 0, this.xSize, this.ySize);
    }

    /**
     * Vypis retazcov "Wallet", "Diamonds", "BTC:" do okna
     * @param mouseX nevyuzity argument
     * @param mouseY nevyuzity argument
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        FontRenderer fontRenderer = this.minecraft.fontRenderer;
        fontRenderer.drawString("Wallet", WALLET_TEXT_X_POS, WALLET_TEXT_Y_POS, Color.BLACK.getRGB());
        fontRenderer.drawString("Diamonds", DIAMONDS_TEXT_X_POS, DIAMONDS_TEXT_Y_POS, Color.BLACK.getRGB());

        if(this.container.getWalletItemStack().isEmpty()){
            return;
        }

        fontRenderer.drawString("BTC: " + this.bitcoinAmount, BITCOIN_AMOUNT_TEXT_X_POS, BITCOIN_AMOUNT_TEXT_Y_POS, Color.BLACK.getRGB());
    }

    @Override
    public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
        this.renderBackground();
        super.render(p_render_1_, p_render_2_, p_render_3_);
        this.renderHoveredToolTip(p_render_1_, p_render_2_);
    }

//    private int ticks = 0;
//    @Override
//    public void tick() {
//        super.tick();
//        if((ticks%200) == 0){
//            this.refresh();
//        }
//        this.ticks++;
//    }


    /**
     * Implementuje rozhranie IRefreshable. Tato funkcia zisti pocet Bitcoinov na Bitcoin adrese vo vlozenom Wallet Iteme
     */
    @Override
    public void refresh() {
        Tuple<Long, Long> pair = WalletItem.getKeyPairFromWalletItemStack(miningBlockContainer.getWalletItemStack());
        this.bitcoinAmount = transactionsCapability.getTransactionsSum(pair.getB());
    }
}
