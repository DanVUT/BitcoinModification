package sk.tuke.bitcoinmod.blockchainblock;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import sk.tuke.bitcoinmod.listwidget.ListWidget;
import sk.tuke.bitcoinmod.transactionscapability.TransactionsCapability;
import sk.tuke.bitcoinmod.transactionscapability.TransactionsCapabilityProvider;

import java.util.Optional;

/**
 * Trieda implementujuca GUI Blockchain Blocku.
 */
@OnlyIn(Dist.CLIENT)
public class BlockchainBlockScreen extends Screen {
    private ListWidget listWidget;
    private TransactionsCapability transactionsCapability;


    /**
     * V konstruktore sa ulozi do lokalnej premennej instancia triedy TransactionsCapability k tomu, aby bola dostupna pre zobrazenie listu transakcii.
     * @param titleIn
     */
    protected BlockchainBlockScreen(ITextComponent titleIn) {
        super(titleIn);

        Optional<ClientWorld> clientWorld = LogicalSidedProvider.CLIENTWORLD.get(LogicalSide.CLIENT);
        transactionsCapability = clientWorld.get().getCapability(TransactionsCapabilityProvider.CAPABILITY_TRANSACTIONS, null).orElse(null);
    }

    /**
     * Metoda init inicializuje listove zobrazenie transakcii
     */
    @Override
    protected void init() {
        super.init();
        this.listWidget = new ListWidget(this.minecraft, transactionsCapability.getAllTransactions(), this.width, this.height, 32, this.height - 32, 85);
        this.children.add(listWidget);
    }


    /**
     * Metoda render sa stara o vykreslenie obsahu okna. V tomto pripade sa vykresluje iba listove zobrazenie
     * @param p_render_1_ X suradnica pozicie mysi
     * @param p_render_2_ Y suradnica pozicie mysi
     * @param p_render_3_ Partial ticks
     */
    @Override
    public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
        super.render(p_render_1_, p_render_2_, p_render_3_);
        this.listWidget.render(p_render_1_, p_render_2_, p_render_3_);
    }

    /**
     * Metoda definuje, ci tato obrazovka ma v rezime jedneho hraca pozastavit hru alebo nie
     * @return Vracia vzdy false. Takze obrazovka hru na pozadi nepozastavi
     */
    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
