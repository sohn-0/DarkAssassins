package com.rs.game.minigames.duel;

import com.rs.Settings;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.cores.CoresManager;
import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.ForceTalk;
import com.rs.game.Graphics;
import com.rs.game.Hit;
import com.rs.game.SecondaryBar;
import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.Hit.HitLook;
import com.rs.game.item.Item;
import com.rs.game.item.ItemsContainer;
import com.rs.game.minigames.zombies.Zombies;
import com.rs.game.npc.Drop.Rarity;
import com.rs.game.npc.combat.NPCCombat;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.npc.familiar.Familiar;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.SlayerManager;
import com.rs.game.player.actions.HerbCleaning;
import com.rs.game.player.content.Assassins;
import com.rs.game.player.content.Burying;
import com.rs.game.player.content.FriendChatsManager;
import com.rs.game.player.content.custom.ItemManager;
import com.rs.game.player.controlers.FightCaves;
import com.rs.game.player.controlers.FightKiln;
import com.rs.game.player.controlers.Wilderness;
import com.rs.game.player.controlers.darkinvasion.DarkInvasion;
import com.rs.game.player.controlers.dung.RuneDungGame;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Logger;
import com.rs.utils.MapAreas;
import com.rs.utils.Misc;
import com.rs.utils.NPCBonuses;
import com.rs.utils.NPCCombatDefinitionsL;
import com.rs.utils.NPCDrops;
import com.rs.utils.Utils;

public class DuelRules {

    private transient Player player, target;
    private transient boolean hasRewardGiven;
    public boolean[] duelRules = new boolean[26];
    private transient ItemsContainer<Item> stake;

    public DuelRules(Player player, Player target) {
	this.player = player;
	this.target = target;
	this.stake = new ItemsContainer<Item>(28, false);
    }
	

    public boolean canAccept(ItemsContainer<Item> stake) {
	if (getRule(0) && getRule(1) && getRule(2)) {
	    player.getPackets().sendGameMessage("You have to be able to use atleast one combat style in a duel.", true);
	    return false;
	}
	if(!getRule(24) && (player.getFamiliar() != null || getTarget().getFamiliar() != null)) {
	    	player.getPackets().sendGameMessage("Summoning has been disabled during this duel!");
	    	getTarget().getPackets().sendGameMessage("Summoning has been disabled during this duel!");
		return false;
	}
	int count = 0;
	Item item;
	for (int i = 10; i < 24; i++) {
	    int slot = i - 10;
	    if (getRule(i) && (item = player.getEquipment().getItem(slot)) != null) {
		if (i == 23) {// arrows
		    if (!(item.getDefinitions().isStackable() && player.getInventory().getItems().containsOne(item)))
			count++;
		} else {
		    count++;
		}
	    }
	}
	int freeSlots = player.getInventory().getItems().freeSlots() - count;
	if (freeSlots < 0) {
	    player.getPackets().sendGameMessage("You do not have enough inventory space to remove all the equipment.");
	    getTarget().getPackets().sendGameMessage("Your opponent does not have enough space to remove all the equipment.");
	    return false;
	}
	for (int i = 0; i < stake.getSize(); i++) {
	    if (stake.get(i) != null) {
		freeSlots--;
	    }
	}
	if (freeSlots < 0) {
	    player.getPackets().sendGameMessage("You do not have enough room in your inventory for this stake.");
	    getTarget().getPackets().sendGameMessage("Your opponent does not have enough room in his inventory for this stake.");
	    return false;
	}
	return true;
    }

    public void setRules(int ruleId) {
	setRules(ruleId, true);
    }

    public void setRules(int ruleId, boolean updated) {
	if (!getRule(ruleId))
	    setRule(ruleId, true);
	else if (getRule(ruleId))
	    setRule(ruleId, false);
	if (updated) {
	    DuelRules rules = getTarget().getLastDuelRules();
	    if (rules == null)
		return;
	    rules.setRules(ruleId, false);
	}
	setConfigs();
	((DuelArena) target.getControlerManager().getControler()).cancelAccepted();
    }

    public void setConfigs() {
	int value = 0;
	int ruleId = 16;
	for (int i = 0; i < duelRules.length; i++) {
	    if (getRule(i)) {
		if (i == 7) // forfiet
		    value += 5;
		if (i == 25) // no movement
		    value += 6;
		value += ruleId;
	    }
	    ruleId += ruleId;
	}
	player.getVarsManager().sendVar(286, value);
    }

    public boolean setRule(int ruleId, boolean value) {
	return duelRules[ruleId] = value;
    }

    public boolean getRule(int ruleId) {
	return duelRules[ruleId];
    }

    public ItemsContainer<Item> getStake() {
	return stake;
    }

    public Player getTarget() {
	return target;
    }

    public boolean hasRewardGiven() {
	return hasRewardGiven;
    }

    public void setRewardGiven(boolean hasGiven) {
	this.hasRewardGiven = hasGiven;
    }

    public static void sendRewardGivenUpdate(Player victor, Player loser, boolean hasGiven) {
	victor.getLastDuelRules().setRewardGiven(hasGiven);
	loser.getLastDuelRules().setRewardGiven(hasGiven);
    }
}
