package sk.tuke.bitcoinmod.walletblock;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkInstance;
import sk.tuke.bitcoinmod.EntryPoint;
import sk.tuke.bitcoinmod.communication.CommunicationChannel;
import sk.tuke.bitcoinmod.communication.generatewallet.GenerateWalletMessageToServer;
import sk.tuke.bitcoinmod.keyscapability.KeysCapability;
import sk.tuke.bitcoinmod.keyscapability.KeysCapabilityProvider;
import sk.tuke.bitcoinmod.walletitem.WalletItem;

public class WalletBlockScreen extends ContainerScreen<WalletBlockContainer> {
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(EntryPoint.MODID, "textures/gui/wallet_block_gui.png");
    private WalletBlockContainer container;
    private KeysCapability keysCapability;
    private static final int GEN_BUTTON_X_POS = 29;
    private static final int GEN_BUTTON_Y_POS = 9;

    private static final int AMOUNT_X_POS = 8;
    private static final int AMOUNT_Y_POS = 60;

    private static final int ADDRESS_X_POS = AMOUNT_X_POS;
    private static final int ADDRESS_Y_POS = AMOUNT_Y_POS + 30;

    private static final int SEND_BUTTON_X_POS = ADDRESS_X_POS + 110;
    private static final int SEND_BUTTON_Y_POS = ADDRESS_Y_POS;

    private TextFieldWidget amountTextField;
    private TextFieldWidget addressTextField;

    public WalletBlockScreen(WalletBlockContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        xSize = 175;
        ySize = 200;
        this.container = screenContainer;
        this.keysCapability = inv.player.getEntityWorld().getCapability(KeysCapabilityProvider.CAPABILITY_KEYS, null).orElse(null);
    }

    @Override
    protected void init() {
        super.init();
        setButtons();
        setTextFields();
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);

        for(Widget button : buttons){
            button.render(mouseX, mouseY, partialTicks);
        }

        this.amountTextField.render(mouseX, mouseY, partialTicks);
        this.addressTextField.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }


    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(BACKGROUND_TEXTURE);
        int edgeSpacingX = (this.width - this.xSize) / 2;
        int edgeSpacingY = (this.height - this.ySize) / 2;
        this.blit(edgeSpacingX, edgeSpacingY, 0, 0, this.xSize, this.ySize);
    }

    private void setTextFields(){
        amountTextField = new TextFieldWidget(minecraft.fontRenderer, this.guiLeft + AMOUNT_X_POS, this.guiTop + AMOUNT_Y_POS, 80, 20, "0.0");
        addressTextField = new TextFieldWidget(minecraft.fontRenderer, this.guiLeft + ADDRESS_X_POS, this.guiTop + ADDRESS_Y_POS, 100, 20, "Address");
        amountTextField.setEnabled(true);
        addressTextField.setEnabled(true);
        amountTextField.setVisible(true);
        addressTextField.setVisible(true);
        children.add(amountTextField);
        children.add(addressTextField);
    }

    private void setButtons(){
        Button generateButton = new Button(GEN_BUTTON_X_POS + this.guiLeft, GEN_BUTTON_Y_POS + this.guiTop, 100, 20, "Generate New Pair", (but) ->{
            CommunicationChannel.SIMPLECHANNEL.sendToServer(new GenerateWalletMessageToServer());
        });
        generateButton.visible = true;
        addButton(generateButton);

        Button sendButton = new Button(SEND_BUTTON_X_POS + this.guiLeft, SEND_BUTTON_Y_POS + this.guiTop, 50, 20, "Send", (but) -> {});
        sendButton.visible = true;
        addButton(sendButton);
    }
}
