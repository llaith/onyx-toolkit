/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.pattern.display.memo.impl;

import com.google.common.base.Strings;
import org.llaith.onyx.toolkit.pattern.display.memo.Block;
import org.llaith.onyx.toolkit.pattern.display.memo.ListBlock;
import org.llaith.onyx.toolkit.pattern.display.memo.ListItem;
import org.llaith.onyx.toolkit.pattern.display.memo.Memo;
import org.llaith.onyx.toolkit.pattern.display.memo.MemoFactory;
import org.llaith.onyx.toolkit.pattern.display.memo.MemoRenderer;
import org.llaith.onyx.toolkit.pattern.display.memo.ParagraphBlock;
import org.llaith.onyx.toolkit.pattern.display.memo.Section;
import org.llaith.onyx.toolkit.pattern.display.memo.TableBlock;
import org.llaith.onyx.toolkit.etc.holder.Counter;
import org.llaith.onyx.toolkit.etc.holder.FirstFlag;
import org.llaith.onyx.toolkit.exception.ExceptionUtil;
import org.llaith.onyx.toolkit.exception.UncheckedException;
import org.llaith.onyx.toolkit.lang.FlowableText;
import org.llaith.onyx.toolkit.lang.StringUtil;

/**
 * Note, can render multiple messages through this if you want.
 */
public class TextMemoRenderer implements MemoRenderer<FlowableText> {

    private final int maxWidth;

    private final int tabSize;

    public TextMemoRenderer(final int maxWidth, final int tabSize) {

        this.maxWidth = maxWidth;

        this.tabSize = tabSize;

    }

    @Override
    public FlowableText render(final Memo memo) {

        final FlowableText text = new FlowableText(this.maxWidth);

        final Counter sectionCounter = new Counter();

        for (final Section section : memo.sections()) {

            if (sectionCounter.notFirst()) text.newline(); // extra

            this.renderSection(text, section);
        }

        return text;

    }

    private void renderSection(final FlowableText text, final Section section) {

        if (section.hasHeading()) {

            text.newline();

            text.newline(
                    this.tabSize * (section.level() * 2),
                    section.heading());

            text.newline(
                    this.tabSize * (section.level() * 2),
                    Strings.repeat("~", section.heading().length()));

            text.newline(); // extra

        }

        final FirstFlag blockFlag = new FirstFlag();

        if (section.hasBlocks()) {

            for (final Block block : section.blocks()) {

                if (blockFlag.notFirst()) text.newline();

                this.renderBlock(
                        text,
                        section.level() * 2,
                        block);

            }
        }

    }

    private void renderBlock(final FlowableText text, final int indentLevel, final Block block) {

        if (block.hasTitle()) {

            text.newline(
                    this.tabSize * indentLevel,
                    block.title() + ":");

        }

        if (block instanceof ParagraphBlock) this.renderParagraph(text, indentLevel, (ParagraphBlock)block);
        else if (block instanceof ListBlock) this.renderList(text, indentLevel, (ListBlock)block);
        else if (block instanceof TableBlock) this.renderTable(text, indentLevel, (TableBlock)block);
        else throw new UncheckedException("Unknown case for: " + block.getClass());

    }

    private void renderParagraph(final FlowableText text, final int indentLevel, final ParagraphBlock para) {

        for (final String line : para.texts()) {

            text.newline(
                    this.tabSize * indentLevel,
                    line);

        }

    }

    private void renderList(final FlowableText text, final int indentLevel, final ListBlock list) {

        final Counter count = new Counter();

        for (final ListItem item : list.items()) {

            this.renderItem(
                    text,
                    indentLevel,
                    list,
                    count.nextValue(),
                    item);

        }

    }

    private void renderItem(final FlowableText text, final int indentLevel, final ListBlock list, final int itemCount, final ListItem item) {

        final String s = list.numeric() ?
                "" + (itemCount + 1) :
                "*";

        text.newline(
                this.tabSize * indentLevel,
                s + ") " + item.heading());

        if (item.hasBlocks()) {

            for (final Block block : item.blocks()) {
                this.renderBlock(text, indentLevel + 1, block); // increment the indent
            }

        }

    }

    private void renderTable(final FlowableText text, final int indentLevel, final TableBlock table) {
        throw new UnsupportedOperationException("TODO");
    }

    public static void main(String[] args) {

        //testBasic();

        //testBasicOverflow();

        //testBasicOverflowWithIndent();

        //testBasicOverflowWithIndentAndLists();

        //testBasicLists();

        testNestedLists();

        try {
            throw new RuntimeException();
        } catch (Exception e) {
            try {
                throw new RuntimeException(e);
            } catch (Exception e1) {
                System.out.println(StringUtil.reflowText(ExceptionUtil.stackTraceToString(e1), 80));
            }
        }

    }

    private static void testBasic() {
        // test basic message
        System.out.println("****************************************************************************************************");
        System.out.println(new TextMemoRenderer(80, 4)
                                   .render(MemoFactory.memo("Hello There!"))
                                   .asString());
    }

    private static void testBasicOverflow() {
        // test basic overflow
        System.out.println("****************************************************************************************************");
        System.out.println(new TextMemoRenderer(80, 4)
                                   .render(MemoFactory.memo("...................................................................................................."))
                                   .asString());
    }

    private static void testBasicOverflowWithIndent() {
        // test basic indent with overflow
        System.out.println("****************************************************************************************************");
        System.out.println(new TextMemoRenderer(80, 4)
                                   .render(new Memo(
                                           new Section(0, "Heading 1",
                                                       new ParagraphBlock("Title 1", "...................................................................................................."),
                                                       new ParagraphBlock("Title 2", "....................................................................................................")),
                                           new Section(1, "Sub Heading 1",
                                                       new ParagraphBlock("Title 3", "...................................................................................................."),
                                                       new ParagraphBlock("Title 4", "....................................................................................................")),
                                           new Section(0, "Heading 2",
                                                       new ParagraphBlock("Title 5", "...................................................................................................."),
                                                       new ParagraphBlock("Title 6", "...................................................................................................."))
                                   ))
                                   .asString());
    }

    private static void testBasicLists() {
        System.out.println("****************************************************************************************************");
        System.out.println(new TextMemoRenderer(80, 4)
                                   .render(new Memo(
                                           new Section(1, "Heading 1",
                                                       new ParagraphBlock("Title 1", "...................................................................................................."),
                                                       new ListBlock(true,
                                                                     "Some List 1",
                                                                     new ListItem("Item 1"),
                                                                     new ListItem("Item 2")))
                                   ))
                                   .asString());
    }

    private static void testNestedLists() {
        System.out.println("****************************************************************************************************");

        final TextMemoRenderer renderer = new TextMemoRenderer(80, 4);

        final Memo memo = new Memo(
                new Section(1, "Heading 1",
                            new ParagraphBlock("Title 1", "...................................................................................................."),
                            new ListBlock(true,
                                          lorum(),
                                          new ListItem("Item 1",
                                                       new ParagraphBlock("Heading", "This is some text.", "This is some more text.")),
                                          new ListItem("Item 2",
                                                       new ListBlock(false, "Sublist",
                                                                     new ListItem("Item A"),
                                                                     new ListItem("Item B")))))
        );

        FlowableText text = renderer.render(memo);

        final String rendered = text.asString();

        System.out.println(rendered);
    }

    private static String lorum() {
        return "Didn't need no welfare states. Everybody pulled his weight. Gee our old Lasalle ran great. Those were the days. And when the odds are against him and their dangers work to do. You bet your life Speed Racer he will see it through. Movin' on up to the east side. We finally got a piece of the pie. Movin' on up to the east side. We finally got a piece of the pie." +
                "Well we're movin' on up to the east side. To a deluxe apartment in the sky. Movin' on up to the east side. We finally got a piece of the pie. The first mate and his Skipper too will do their very best to make the others comfortable in their tropic island nest. The first mate and his Skipper too will do their very best to make the others comfortable in their tropic island nest. Sunny Days sweepin' the clouds away. On my way to where the air is sweet. Can you tell me how to get how to get to Sesame Street." +
                "\nAnd when the odds are against him and their dangers work to do. You bet your life Speed Racer he will see it through. He's gainin' on you so you better look alive. He busy revin' up his Powerful Mach 5. The ship set ground on the shore of this uncharted desert isle with Gilligan the Skipper too the millionaire and his wife. So this is the tale of our castaways they're here for a long long time. They'll have to make the best of things its an uphill climb. Straightnin' the curves. Flatnin' the hills Someday the mountain might get ‘em but the law never will. It's time to play the music. It's time to light the lights. It's time to meet the Muppets on the Muppet Show tonight." +
                "Just two good ol' boys Wouldn't change if they could. Fightin' the system like a true modern day Robin Hood. So lets make the most of this beautiful day. Since we're together! Movin' on up to the east side. We finally got a piece of the pie. Maybe you and me were never meant to be. But baby think of me once in awhile. I'm at WKRP in Cincinnati. Sunny Days sweepin' the clouds away. On my way to where the air is sweet. Can you tell me how to get how to get to Sesame Street." +
                "All of them had hair of gold like their mother the youngest one in curls. Its mission - to explore strange new worlds to seek out new life and new civilizations to boldly go where no man has gone before. Come and knock on our door. We've been waiting for you. Where the kisses are hers and hers and his. Three's company too. Here he comes Here comes Speed Racer. He's a demon on wheels? Here's the story of a man named Brady who was busy with three boys of his own. And when the odds are against him and their dangers work to do. You bet your life Speed Racer he will see it through. Just sit right back and you'll hear a tale a tale of a fateful trip that started from this tropic port aboard this tiny ship";
    }

}
