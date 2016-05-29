package com.rs.game.player.quest.impl;

import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.utils.Utils;

public class HalloweenEvent {
	
	/**
	 * @author Mario (AlterOPS)
	 **/

	static Player player;
	
	public HalloweenEvent(Player player) {
		this.player = player;
	}
	

	public static void startEvent() {
		World.spawnObject(new WorldObject(62621, 10, 1, 3108, 3260, 0), true);
		World.spawnNPC(12377, new WorldTile(3108, 3265, 0), 1, true);
		World.spawnNPC(12378, new WorldTile(4315, 5470, 0), 1, true);
		World.spawnNPC(12392, new WorldTile(4313, 5462, 0), 1, true);
		World.spawnNPC(14398, new WorldTile(3107, 3261, 0), 1, true);
		World.spawnNPC(14398, new WorldTile(3107, 3265, 0), 1, true);
		World.spawnNPC(14398, new WorldTile(3108, 3265, 0), 1, true);
		World.spawnNPC(12379, new WorldTile(4311, 5478, 0), 1, true);
		World.spawnNPC(12375, new WorldTile(4311, 5465, 0), 1, true);
		World.spawnNPC(14400, new WorldTile(4318, 5464, 0), 1, true);
		World.spawnNPC(14389, new WorldTile(4330, 5460, 0), 1, true);
		World.sendWorldMessage("<col=228B22>Halloween event hast started in Draynor! Happy Halloween!", false);
	}


}