package io.github.darkkronicle.advancedchatlog;

import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.GuiTextFieldGeneric;
import fi.dy.masa.malilib.util.StringUtils;
import io.github.darkkronicle.advancedchatcore.chat.ChatMessage;
import io.github.darkkronicle.advancedchatcore.util.ColorUtil;
import io.github.darkkronicle.advancedchatcore.util.EasingMethod;
import io.github.darkkronicle.advancedchatcore.util.FindType;
import io.github.darkkronicle.advancedchatcore.util.RawText;
import io.github.darkkronicle.advancedchatcore.util.SearchUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class ChatLogScreen extends GuiBase {

    private double scrollStart = 0;
    private double scrollEnd = 0;
    private double currentScroll = 0;
    private long lastScrollTime = 0;
    private int scrollTimeMs = 500;

    private List<ChatMessage.AdvancedChatLine> renderLines;
    private GuiTextFieldGeneric search = null;

    public ChatLogScreen() {
        super();
    }

    @Override
    public void initGui() {
        super.initGui();
        setLines(ChatLogData.getInstance().getMessages());
        int width = client.getWindow().getScaledWidth();
        int height = client.getWindow().getScaledHeight();
        search = new GuiTextFieldGeneric((width / 2) - 40, 10, 80, 12, textRenderer);
        addTextField(search, (textField -> {
            searchText(textField.getText());
            return true;
        }));
    }

    private void searchText(String contents) {
        if (contents.isEmpty()) {
            setLines(ChatLogData.getInstance().getMessages());
            return;
        }
        List<ChatMessage> sorted = new ArrayList<>();
        for (ChatMessage m : ChatLogData.getInstance().getMessages()) {
            if (SearchUtils.isMatch(m.getDisplayText().getString(), contents, FindType.LITERAL)) {
                sorted.add(m);
            }
        }
        setLines(sorted);
    }

    private void setLines(List<ChatMessage> messages) {
        // Don't want jank
        messages = new ArrayList<>(messages);
        if (messages.isEmpty()) {
            Text text = RawText.withStyle(StringUtils.translate("advancedchatlog.message.none"), Style.EMPTY.withColor(TextColor.fromFormatting(Formatting.RED)));
            messages.add(
                    ChatMessage
                            .builder()
                            .displayText(text)
                            .originalText(text)
                            .build()
            );
        }
        renderLines = new ArrayList<>();
        for (ChatMessage m : messages) {
            for (int i = m.getLineCount() - 1; i >= 0; i--) {
                renderLines.add(m.getLines().get(i));
            }
        }
    }

    private void updateScroll() {
        long time = Util.getMeasuringTimeMs();
        // Starting scroll + percent completed
        currentScroll = scrollStart + ((scrollEnd - scrollStart) * (1 - EasingMethod.Method.SINE.apply(1 - ((float) time - lastScrollTime) / scrollTimeMs)));
        int fontHeight = (textRenderer.fontHeight + 2);
        if (currentScroll < fontHeight) {
            // Make sure we can still see at least one line
            currentScroll = fontHeight;
            scrollEnd = 0;
            lastScrollTime = 0;
        }
        int maxY = fontHeight * renderLines.size() - 1;
        if (currentScroll >= maxY) {
            // Make sure it stops at the top
            currentScroll = maxY;
            scrollEnd = maxY;
            lastScrollTime = 0;
        }
    }

    @Override
    public boolean onMouseScrolled(int mouseX, int mouseY, double mouseWheelDelta) {
        if (super.onMouseScrolled(mouseX, mouseY, mouseWheelDelta)) {
            return true;
        }
        // Update the scroll variables
        scrollEnd = currentScroll + mouseWheelDelta * 100;
        scrollStart = currentScroll;
        lastScrollTime = Util.getMeasuringTimeMs();
        return true;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        updateScroll();
        int height = client.getWindow().getScaledHeight();
        int width = client.getWindow().getScaledWidth();
        int lineHeight = textRenderer.fontHeight + 2;
        // 60 px top, 40 px bottom
        int lines = (int) Math.ceil((float) (height - 70 - lineHeight) / (lineHeight));

        int scrollLine = (int) Math.floor((float) currentScroll / (lineHeight));
        int y = -1 * ((int) currentScroll % lineHeight);

        // Scissor to keep boundaries for the half scroll
        double scale = client.getWindow().getScaleFactor();
        ScissorUtil.applyScissor(0, (int) (40 * scale), (int) (width * scale), (int) ((height - 70) * scale));

        for (int i = scrollLine; i < scrollLine + lines; i++) {
            if (i >= renderLines.size()) {
                break;
            }
            ChatMessage.AdvancedChatLine line = renderLines.get(i);
            client.textRenderer.drawWithShadow(matrixStack, line.getText(), 10, height - y - 40 - fontHeight, ColorUtil.WHITE.color());
            y += lineHeight;
        }
        ScissorUtil.resetScissor();
    }
}
