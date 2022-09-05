package com.example.demo;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class TextController {

    public static void main(String[] args) {
        String text = "<h3>TEST Text</h3>\n" +
                "<ul>\n" +
                "<li>test</li>\n" +
                "<li>test</li>\n" +
                "</ul>";
        String text2 = "### TEST Text\n" +
                "* test\n" +
                "* test";
        System.out.println("toHTML(text2) = " + convertMarkdownToHTML(text2));

        String s = text.replaceAll("n", "<br>");
        System.out.println(s);
    }

    public static String convertMarkdownToHTML(String content) {
        List<org.commonmark.Extension> extensions = new ArrayList<>();
        extensions.add(TablesExtension.create());
        extensions.add(org.commonmark.ext.gfm.strikethrough.StrikethroughExtension.create());
        extensions.add(org.commonmark.ext.autolink.AutolinkExtension.create());
        Parser parser = Parser.builder()
                .build();
        HtmlRenderer renderer = HtmlRenderer.builder()
                .softbreak("<br>")
                .build();
        Node document = parser.parse(content == null ? "" : content);
        return renderer.render(document);
    }

    @GetMapping("/")
    public String setText(Model model){
        String text = "# Markdown syntax guide\n" +
                "\n" +
                "## Headers\n" +
                "\n" +
                "# This is a Heading h1\n" +
                "## This is a Heading h2 \n" +
                "###### This is a Heading h6\n" +
                "\n" +
                "## Emphasis\n" +
                "\n" +
                "*This text will be italic*  \n" +
                "_This will also be italic_\n" +
                "\n" +
                "**This text will be bold**  \n" +
                "__This will also be bold__\n" +
                "\n" +
                "_You **can** combine them_\n" +
                "\n" +
                "## Lists\n" +
                "\n" +
                "### Unordered\n" +
                "\n" +
                "* Item 1\n" +
                "* Item 2\n" +
                "* Item 2a\n" +
                "* Item 2b\n" +
                "\n" +
                "### Ordered\n" +
                "\n" +
                "1. Item 1\n" +
                "1. Item 2\n" +
                "1. Item 3\n" +
                "  1. Item 3a\n" +
                "  1. Item 3b\n" +
                "\n" +
                "## Images\n" +
                "\n" +
                "![This is a alt text.](/image/sample.png \"This is a sample image.\")\n" +
                "\n" +
                "## Links\n" +
                "\n" +
                "You may be using [Markdown Live Preview](https://markdownlivepreview.com/).\n" +
                "\n" +
                "## Blockquotes\n" +
                "\n" +
                "> Markdown is a lightweight markup language with plain-text-formatting syntax, created in 2004 by John Gruber with Aaron Swartz.\n" +
                ">\n" +
                ">> Markdown is often used to format readme files, for writing messages in online discussion forums, and to create rich text using a plain text editor.\n" +
                "\n" +
                "## Tables\n" +
                "\n" +
                "| Left columns  | Right columns |\n" +
                "| ------------- |:-------------:|\n" +
                "| left foo      | right foo     |\n" +
                "| left bar      | right bar     |\n" +
                "| left baz      | right baz     |\n" +
                "\n" +
                "## Blocks of code\n" +
                "\n" +
                "```\n" +
                "let message = 'Hello world';\n" +
                "alert(message);\n" +
                "```\n" +
                "\n" +
                "## Inline code\n" +
                "\n" +
                "This web site is using `markedjs/marked`.";
        String result = convertMarkdownToHTML(text);
        model.addAttribute("text", result);
        return "note_read";
    }
}
