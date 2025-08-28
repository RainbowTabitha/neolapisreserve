package tf.ssf.sfort.lapisreserve.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tf.ssf.sfort.lapisreserve.PlayerInterface;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerReserve extends Player {
	public ServerPlayerReserve(Level level, BlockPos pos, float yaw, GameProfile gameProfile) {
		super(level, pos, yaw, gameProfile);
	}

	@Inject(method = "restoreFrom",at=@At("TAIL"))
	public void copyFrom(ServerPlayer oldPlayer, boolean alive, CallbackInfo info) {
		((PlayerInterface)getInventory()).setLapisreserve(((PlayerInterface)oldPlayer.getInventory()).getLapisreserve());
	}
}
