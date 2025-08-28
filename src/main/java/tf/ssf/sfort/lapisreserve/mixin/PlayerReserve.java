package tf.ssf.sfort.lapisreserve.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tf.ssf.sfort.lapisreserve.PlayerInterface;

@Mixin(Inventory.class)
public class PlayerReserve implements PlayerInterface {
	@Dynamic
	public ItemStack lapisreserve = ItemStack.EMPTY;
	@Shadow
	public Player player;

	@Inject(method = "save(Lnet/minecraft/nbt/ListTag;)Lnet/minecraft/nbt/ListTag;", at=@At("HEAD"))
	public void serialize(ListTag tag, CallbackInfoReturnable<ListTag> info) {
		if (lapisreserve.isEmpty()) return;
		CompoundTag compoundTag = new CompoundTag();
		compoundTag.putByte("LapisReserve", (byte)0);
	
		CompoundTag itemTag = new CompoundTag();
		lapisreserve.save(player.level().registryAccess(), itemTag);
		compoundTag.put("Item", itemTag);
	
		tag.add(compoundTag);
	}
	
	@Inject(method = "load(Lnet/minecraft/nbt/ListTag;)V", at=@At("HEAD"))
	public void deserialize(ListTag tag, CallbackInfo info) {
		for (int i = 0; i < tag.size(); ++i) {
			CompoundTag compoundTag = tag.getCompound(i);
			if (compoundTag.contains("LapisReserve")) {
				if (compoundTag.contains("Item")) {
					lapisreserve = ItemStack.parse(player.level().registryAccess(), compoundTag.getCompound("Item"))
										   .orElse(ItemStack.EMPTY);
				}
				tag.remove(i);
				break;
			}
		}
	}

	@Override public ItemStack getLapisreserve(){ return lapisreserve; }
	@Override public void setLapisreserve(ItemStack stack) { lapisreserve = stack; }
}
