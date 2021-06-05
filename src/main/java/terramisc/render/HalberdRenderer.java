package terramisc.render;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import terramisc.items.tools.ItemCustomHalberd;
import org.lwjgl.opengl.GL11;

/**
 * 05-06-21
 * forked from Extrafirma
 * @author Swarg
 */
public class HalberdRenderer implements IItemRenderer {

   private static final ResourceLocation ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
   private Minecraft mc;
   private RenderItem itemRenderer;


   public boolean handleRenderType(ItemStack item, IItemRenderer.ItemRenderType type) {
      return type == IItemRenderer.ItemRenderType.INVENTORY || type == IItemRenderer.ItemRenderType.EQUIPPED || type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON;
   }

   public boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType type, ItemStack item, IItemRenderer.ItemRendererHelper helper) {
      return false;
   }

   public void renderItem(IItemRenderer.ItemRenderType type, ItemStack item, Object ... data) {
      GL11.glPushMatrix();
      if (this.mc == null) {
         this.mc = FMLClientHandler.instance().getClient();
         this.itemRenderer = new RenderItem();
      }

      this.mc.renderEngine.bindTexture(TextureMap.locationItemsTexture);
      Tessellator tessellator = Tessellator.instance;
      IIcon icon;
      //отрисовка длинного шеста алебарды
      if (type == IItemRenderer.ItemRenderType.EQUIPPED) {
         GL11.glTranslatef(-0.5F, -0.5F, 0.0F);
         GL11.glScalef(2.0F, 2.0F, 1.0F);
         icon = ((ItemCustomHalberd)item.getItem()).bigIcon;
         ItemRenderer.renderItemIn2D(tessellator, icon.getMaxU(), icon.getMinV(), icon.getMinU(), icon.getMaxV(), icon.getIconWidth(), icon.getIconHeight(), 0.0625F);
      }
      else if(type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON) {
         icon = item.getIconIndex();
         ItemRenderer.renderItemIn2D(tessellator, icon.getMaxU(), icon.getMinV(), icon.getMinU(), icon.getMaxV(), icon.getIconWidth(), icon.getIconHeight(), 0.0625F);
         if(item != null && item.hasEffect(0)) {
            renderEnchantmentEffects(tessellator);
         }
      }
      else if(type == IItemRenderer.ItemRenderType.INVENTORY) {
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         this.itemRenderer.renderIcon(0, 0, item.getIconIndex(), 16, 16);
      }

      GL11.glPopMatrix();
   }

   public static void renderEnchantmentEffects(Tessellator tessellator) {
      GL11.glDepthFunc(514);
      GL11.glDisable(2896);
      FMLClientHandler.instance().getClient().renderEngine.bindTexture(ITEM_GLINT);
      GL11.glEnable(3042);
      GL11.glBlendFunc(768, 1);
      float f7 = 0.76F;
      GL11.glColor4f(0.38F, 0.19F, 0.608F, 1.0F);
      GL11.glMatrixMode(5890);
      GL11.glPushMatrix();
      float f8 = 0.125F;
      GL11.glScalef(0.125F, 0.125F, 0.125F);
      final long systime = Minecraft.getSystemTime();
      float f9 = (float)(systime % 3000L) / 3000.0F * 8.0F;
      GL11.glTranslatef(f9, 0.0F, 0.0F);
      GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
      ItemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
      GL11.glPopMatrix();
      GL11.glPushMatrix();
      GL11.glScalef(0.125F, 0.125F, 0.125F);
      f9 = (float)(systime % 4873L) / 4873.0F * 8.0F;
      GL11.glTranslatef(-f9, 0.0F, 0.0F);
      GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
      ItemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
      GL11.glPopMatrix();
      GL11.glMatrixMode(5888);
      GL11.glDisable(3042);
      GL11.glEnable(2896);
      GL11.glDepthFunc(515);
   }

}

