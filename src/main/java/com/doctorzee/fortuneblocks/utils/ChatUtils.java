package com.doctorzee.fortuneblocks.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ChatUtils
{

    private final static int CENTER_PX = 154;

    public static void sendCenteredMessage(CommandSender sender, String message)
    {
        if (message == null || message.equals(""))
        {
            sender.sendMessage("");
        }
        message = ChatColor.translateAlternateColorCodes('&', message);

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for (char c : message.toCharArray())
        {
            if (c == ChatColor.COLOR_CHAR)
            {
                previousCode = true;
            }
            else if (previousCode)
            {
                previousCode = false;
                isBold = (c == 'l' || c == 'L');
            }
            else
            {
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while (compensated < toCompensate)
        {
            sb.append(" ");
            compensated += spaceLength;
        }
        sender.sendMessage(sb.toString() + message);
    }

    public static String renderString(String string, String... arguments)
    {
        if (arguments.length % 2 != 0)
        {
            throw new IllegalArgumentException("Must have an even number of arguments.");
        }
        for (int i = 0; i < arguments.length; i += 2)
        {
            string = string.replace(arguments[i], arguments[i + 1]);
        }
        return string;
    }

}
