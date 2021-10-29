/*
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatlog.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ScissorUtil {

    private ScissorUtil() {}

    public static void applyScissor(int x1, int y1, int x2, int y2) {
        GlStateManager._enableScissorTest();
        GlStateManager._scissorBox(x1, y1, x2 - x1, y2 - y1);
    }

    public static void resetScissor() {
        GlStateManager._disableScissorTest();
    }
}
