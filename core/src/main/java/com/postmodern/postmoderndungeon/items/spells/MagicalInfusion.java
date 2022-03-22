/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.postmodern.postmoderndungeon.items.spells;

import com.postmodern.postmoderndungeon.Badges;
import com.postmodern.postmoderndungeon.Dungeon;
import com.postmodern.postmoderndungeon.Statistics;
import com.postmodern.postmoderndungeon.actors.buffs.Degrade;
import com.postmodern.postmoderndungeon.actors.hero.Talent;
import com.postmodern.postmoderndungeon.items.Item;
import com.postmodern.postmoderndungeon.items.armor.Armor;
import com.postmodern.postmoderndungeon.items.scrolls.ScrollOfUpgrade;
import com.postmodern.postmoderndungeon.items.weapon.Weapon;
import com.postmodern.postmoderndungeon.messages.Messages;
import com.postmodern.postmoderndungeon.sprites.ItemSpriteSheet;
import com.postmodern.postmoderndungeon.utils.GLog;

public class MagicalInfusion extends InventorySpell {
	
	{
		image = ItemSpriteSheet.MAGIC_INFUSE;

		unique = true;
	}

	@Override
	protected boolean usableOnItem(Item item) {
		return item.isUpgradable();
	}

	@Override
	protected void onItemSelected( Item item ) {

		ScrollOfUpgrade.upgrade(curUser);

		Degrade.detach( curUser, Degrade.class );

		if (item instanceof Weapon && ((Weapon) item).enchantment != null) {
			((Weapon) item).upgrade(true);
		} else if (item instanceof Armor && ((Armor) item).glyph != null) {
			((Armor) item).upgrade(true);
		} else {
			item.upgrade();
		}
		
		GLog.p( Messages.get(this, "infuse", item.name()) );
		Talent.onUpgradeScrollUsed( Dungeon.hero );
		Badges.validateItemLevelAquired(item);

		Statistics.upgradesUsed++;
	}
	
	@Override
	public int value() {
		//prices of ingredients, divided by output quantity
		return Math.round(quantity * ((50 + 40) / 1f));
	}
	
	public static class Recipe extends com.postmodern.postmoderndungeon.items.Recipe.SimpleRecipe {
		
		{
			inputs =  new Class[]{ScrollOfUpgrade.class, ArcaneCatalyst.class};
			inQuantity = new int[]{1, 1};
			
			cost = 3;
			
			output = MagicalInfusion.class;
			outQuantity = 1;
		}
		
	}
}
