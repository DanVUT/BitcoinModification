package sk.tuke.bitcoinmod.walletblock;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sk.tuke.bitcoinmod.EntryPoint;
import sk.tuke.bitcoinmod.communication.CommunicationChannel;
import sk.tuke.bitcoinmod.communication.generatewallet.GenerateWalletMessageToServer;
import sk.tuke.bitcoinmod.communication.newtransactionrequest.NewTransactionRequestToServer;
import sk.tuke.bitcoinmod.interfaces.IRefreshable;
import sk.tuke.bitcoinmod.keyscapability.KeysCapability;
import sk.tuke.bitcoinmod.keyscapability.KeysCapabilityProvider;
import sk.tuke.bitcoinmod.transactionscapability.TransactionsCapability;
import sk.tuke.bitcoinmod.transactionscapability.TransactionsCapabilityProvider;
import sk.tuke.bitcoinmod.walletitem.WalletItem;

import java.awt.*;

@OnlyIn(Dist.CLIENT)
public class WalletBlockScreen extends ContainerScreen<WalletBlockContainer> implements IRefreshable {
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(EntryPoint.MODID, "textures/gui/wallet_block_gui.png");
    private WalletBlockContainer container;
    private KeysCapability keysCapability;
    private TransactionsCapability transactionsCapability;
    private float bitcoinAmount;
    private static final int GEN_BUTTON_X_POS = 29;
    private static final int GEN_BUTTON_Y_POS = 9;

    private static final int AMOUNT_X_POS = 8;
    private static final int AMOUNT_Y_POS = 60;

    private static final int ADDRESS_X_POS = AMOUNT_X_POS;
    private static final int ADDRESS_Y_POS = AMOUNT_Y_POS + 30;

    private static final int SEND_BUTTON_X_POS = ADDRESS_X_POS + 110;
    private static final int SEND_BUTTON_Y_POS = ADDRESS_Y_POS;

    private static final int BITCOIN_BALANCE_TEXT_X_POS = 30;
    private static final int BITCOIN_BALANCE_TEXT_Y_POS = 42;


    private TextFieldWidget amountTextField;
    private TextFieldWidget addressTextField;
    private Button sendButton;

    public WalletBlockScreen(WalletBlockContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        xSize = 175;
        ySize = 200;
        this.container = screenContainer;
        this.keysCapability = inv.player.getEntityWorld().getCapability(KeysCapabilityProvider.CAPABILITY_KEYS, null).orElse(null);
        this.transactionsCapability = inv.player.getEntityWorld().getCapability(TransactionsCapabilityProvider.CAPABILITY_TRANSACTIONS, null).orElse(null);
        this.bitcoinAmount = 0.0f;
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
        this.amountTextField.render(mouseX, mouseY, partialTicks);
        this.addressTextField.render(mouseX, mouseY, partialTicks);
        for(Widget button : buttons){
            button.render(mouseX, mouseY, partialTicks);
        }
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        FontRenderer fontRenderer = this.minecraft.fontRenderer;

        ItemStack managedWallet = this.container.getManagedWalletSlot();
        if(managedWallet.isEmpty()){
            return;
        }
        fontRenderer.drawString("BTC: " + bitcoinAmount, BITCOIN_BALANCE_TEXT_X_POS, BITCOIN_BALANCE_TEXT_Y_POS, Color.BLACK.getRGB());
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(BACKGROUND_TEXTURE);
        int edgeSpacingX = (this.width - this.xSize) / 2;
        int edgeSpacingY = (this.height - this.ySize) / 2;
        this.blit(edgeSpacingX, edgeSpacingY, 0, 0, this.xSize, this.ySize);
    }


    @Override
    public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
        if(amountTextField.isFocused()){
            return amountTextField.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
        }
        if(addressTextField.isFocused()){
            return addressTextField.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
        }
        return super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
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
        Button generateButton = new Button(GEN_BUTTON_X_POS + this.guiLeft, GEN_BUTTON_Y_POS + this.guiTop, 120, 20, "New Bitcoin Key Pair", (but) ->{
            CommunicationChannel.SIMPLECHANNEL.sendToServer(new GenerateWalletMessageToServer());
        });
        generateButton.visible = true;
        addButton(generateButton);

        this.sendButton = new Button(SEND_BUTTON_X_POS + this.guiLeft, SEND_BUTTON_Y_POS + this.guiTop, 50, 20, "Send", (but) -> {
            ItemStack walletStack = container.getManagedWalletSlot();
            if(walletStack.isEmpty()){
                return;
            }
            Tuple<Long, Long> keyPair = WalletItem.getKeyPairFromWalletItemStack(walletStack);
            Long senderPrivateKey = keyPair.getA();
            Long senderAddress = keyPair.getB();
            long recipientAddress;
            float bitcoinAmount;

            try{
                recipientAddress = Long.parseLong(addressTextField.getText(), 16);
                bitcoinAmount = Float.parseFloat(amountTextField.getText());
            } catch (Exception e){
                return;
            }


            CommunicationChannel.SIMPLECHANNEL.sendToServer(new NewTransactionRequestToServer(senderPrivateKey, senderAddress, recipientAddress, bitcoinAmount));

            this.amountTextField.setText("");
            this.addressTextField.setText("");
        });
        sendButton.visible = true;
        addButton(sendButton);
    }

    @Override
    public void tick() {
        super.tick();
        if(this.container.getManagedWalletSlot().isEmpty()){
            this.sendButton.active = false;
            this.amountTextField.setEnabled(false);
            this.addressTextField.setEnabled(false);
        } else {
            this.sendButton.active = true;
            this.amountTextField.setEnabled(true);
            this.addressTextField.setEnabled(true);
        }
        if(amountTextField.isFocused() || !amountTextField.getText().equals("")){
            amountTextField.setSuggestion("");
        } else {
            amountTextField.setSuggestion("Bitcoin Amount");
        }
        if(addressTextField.isFocused() || !addressTextField.getText().equals("")){
            addressTextField.setSuggestion("");
        } else {
            addressTextField.setSuggestion("Recipient Address");
        }
    }


    @Override
    public void refresh() {
        Tuple<Long, Long> pair = WalletItem.getKeyPairFromWalletItemStack(container.getManagedWalletSlot());
        this.bitcoinAmount = transactionsCapability.getTransactionsSum(pair.getB());
    }
}
