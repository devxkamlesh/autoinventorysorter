# Installation Guide

## 📦 Quick Install

### Step 1: Copy the JAR file
```bash
copy "build\libs\autoinventorysorter-1.0.0.jar" "C:\Users\kamle\AppData\Roaming\PrismLauncher\instances\1.21.8\minecraft\mods\"
```

### Step 2: Launch Minecraft
1. Open PrismLauncher
2. Select your 1.21.8 instance
3. Click "Launch"

### Step 3: Verify Installation
Look for this in the log:
```
[AutoInventorySorter] Client ready. Press [R] to sort!
[AutoInventorySorter] Features: Slot Locking, Auto-Refill, Chest Sorting, Blacklist
```

---

## ✅ Requirements

- **Minecraft**: 1.21.8
- **Fabric Loader**: 0.16.9+
- **Fabric API**: 0.136.1+1.21.8
- **Java**: 21+

### Optional Dependencies
- **Mod Menu**: For in-game configuration (recommended)
- **Cloth Config**: For advanced settings UI

---

## 🎮 First Time Setup

### 1. Test Basic Sorting
1. Open your inventory (E)
2. Press **R** key
3. Your inventory should sort instantly!

### 2. Configure Settings
Edit: `.minecraft/config/autoinventorysorter.json`

**Recommended starter config:**
```json
{
  "enabled": true,
  "autoRefillHotbar": true,
  "enableSlotLocking": true,
  "lockSword": true,
  "swordSlot": 0,
  "enableChestSorting": true,
  "showSortButtonInInventory": true
}
```

### 3. Test Chest Sorting
1. Place a chest
2. Fill it with random items
3. Open the chest
4. Press **R** key
5. Chest contents sort by category!

### 4. Test Auto-Refill
1. Put food in hotbar slot 5
2. Put more food in main inventory
3. Eat the food in slot 5
4. Watch it auto-refill from inventory!

---

## 🔧 Configuration

### Default Config Location
```
.minecraft/config/autoinventorysorter.json
```

### First Launch
The mod creates a default config automatically with these settings:
- Sorting enabled
- Auto-sort on inventory open
- Hotbar organization every 2 seconds
- Slot locking for sword, pickaxe, axe, totem
- Auto-refill for food, blocks, torches, arrows

### Customizing
1. Close Minecraft
2. Edit `config/autoinventorysorter.json`
3. Save the file
4. Restart Minecraft

---

## 🎯 Feature Activation

### Enable Slot Locking
```json
{
  "enableSlotLocking": true,
  "lockSword": true,
  "swordSlot": 0
}
```

### Enable Auto-Refill
```json
{
  "autoRefillHotbar": true,
  "refillFood": true,
  "refillBlocks": true
}
```

### Enable Chest Sorting
```json
{
  "enableChestSorting": true,
  "sortChestsWithKey": true
}
```

### Enable Blacklist
```json
{
  "enableBlacklist": true,
  "blacklistedItems": ["minecraft:compass"]
}
```

### Enable Sort Button
```json
{
  "showSortButtonInInventory": true,
  "sortButtonX": 130,
  "sortButtonY": 60
}
```

---

## 🐛 Troubleshooting

### Mod Not Loading?
**Check:**
1. Fabric Loader is installed
2. Fabric API is in mods folder
3. Java 21 is being used
4. Minecraft version is 1.21.8

**View logs:**
```
.minecraft/logs/latest.log
```

Look for errors containing "autoinventorysorter"

### Crashes on Launch?
**Common causes:**
1. Wrong Minecraft version
2. Missing Fabric API
3. Conflicting mods
4. Outdated Java version

**Solution:**
1. Remove all other mods
2. Test with just Fabric API + Auto Inventory Sorter
3. Add other mods back one by one

### R Key Not Working?
**Check:**
1. Key binding in Controls menu
2. Another mod using R key
3. Config has `enabled: true`

**Fix:**
1. Go to Options → Controls
2. Search for "Sort"
3. Rebind to different key if needed

### Sort Button Not Visible?
**Check:**
1. `showSortButtonInInventory: true`
2. Button position not off-screen
3. GUI scale in video settings

**Fix:**
```json
{
  "sortButtonX": 130,
  "sortButtonY": 60
}
```

### Items Not Refilling?
**Check:**
1. `autoRefillHotbar: true`
2. Item type is enabled (e.g., `refillFood: true`)
3. Item exists in main inventory
4. Item not blacklisted

### Chest Sorting Not Working?
**Check:**
1. `enableChestSorting: true`
2. `sortChestsWithKey: true`
3. Pressing R while chest is open
4. Not a locked chest

---

## 🔄 Updating

### From Previous Version
1. Delete old JAR from mods folder
2. Copy new JAR to mods folder
3. Config file is preserved automatically
4. New features use default settings

### Backup Config
Before updating:
```bash
copy ".minecraft\config\autoinventorysorter.json" "autoinventorysorter.json.backup"
```

---

## 🗑️ Uninstalling

### Complete Removal
1. Delete JAR from mods folder
2. Delete config file (optional):
   ```
   .minecraft/config/autoinventorysorter.json
   ```

### Keep Settings
Just remove the JAR file. Config will be preserved for future use.

---

## 📊 Performance Tips

### For Low-End PCs
```json
{
  "organizeHotbar": false,
  "autoRefillHotbar": false,
  "showSortButtonInInventory": false
}
```

### For High-End PCs
```json
{
  "organizeHotbar": true,
  "autoRefillHotbar": true,
  "autoSortOnOpen": true,
  "sortChestsOnOpen": true
}
```

---

## 🎮 Multiplayer

### Client-Side Only
This mod is **client-side only**. It works on:
- ✅ Singleplayer
- ✅ Multiplayer servers
- ✅ Realms
- ✅ LAN worlds

**No server installation needed!**

### Server Compatibility
Works on any server that allows:
- Fabric mods
- Client-side inventory modifications

Most servers allow this mod.

---

## 🤝 Mod Compatibility

### Compatible With
- ✅ Mod Menu
- ✅ Cloth Config
- ✅ REI (Roughly Enough Items)
- ✅ JEI (Just Enough Items)
- ✅ Inventory Profiles Next
- ✅ Mouse Tweaks
- ✅ Xaero's Minimap
- ✅ Litematica

### May Conflict With
- ⚠️ Other inventory sorting mods
- ⚠️ Inventory tweaking mods
- ⚠️ Mods that modify inventory GUI

**Solution:** Disable conflicting features in config

---

## 📝 Notes

- Config changes require restart
- Keybinding changes are instant
- Slot locks apply on next sort
- Blacklist updates on next sort

---

## 🆘 Getting Help

### Before Asking for Help
1. Check this guide
2. Read FEATURES.md
3. Check latest.log for errors
4. Try with minimal mods

### When Reporting Issues
Include:
- Minecraft version
- Fabric Loader version
- Fabric API version
- Mod version
- Other mods installed
- Config file
- Crash log (if crashed)

---

**Happy Sorting! 🎮✨**
