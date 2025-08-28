package tf.ssf.sfort.lapisreserve.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tf.ssf.sfort.lapisreserve.PlayerInterface;

import java.util.List;

@Mixin(EnchantmentMenu.class)
public abstract class EnchantScreen extends AbstractContainerMenu{
	@Shadow @Final @Mutable
	private final ContainerLevelAccess access;
	
	@Shadow
	protected List<Slot> slots;
	
	protected EnchantScreen(MenuType<?> type, int syncId, Container container, ContainerLevelAccess access) {
		super(type, syncId);
		this.access = access;
	}
	
	@Inject(method="<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V", at=@At("RETURN"))
	public void open(int syncId, Inventory playerInventory, ContainerLevelAccess access, CallbackInfo info) {
		if (slots != null && slots.size() > 1) {
			slots.get(1).set(ItemStack.EMPTY);
		}
	}
	
	@Inject(method="removed(Lnet/minecraft/world/entity/player/Player;)V", at=@At("HEAD"), cancellable = true)
	public void close(Player player, CallbackInfo info) {
		if (slots != null && slots.size() > 1) {
			((PlayerInterface)player.getInventory()).setLapisreserve(slots.get(1).getItem());
			slots.get(1).set(ItemStack.EMPTY);
		}
	}
}
