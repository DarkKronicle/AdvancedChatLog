/*
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatlog.util;

import io.github.darkkronicle.advancedchatcore.chat.ChatMessage;
import java.time.LocalDate;
import lombok.Data;

@Data
public class LogChatMessage {

    private final ChatMessage message;

    private final LocalDate date;

    public LogChatMessage(ChatMessage message) {
        this(message, LocalDate.now());
    }

    public LogChatMessage(ChatMessage message, LocalDate date) {
        this.message = message;
        this.date = date;
    }
}
