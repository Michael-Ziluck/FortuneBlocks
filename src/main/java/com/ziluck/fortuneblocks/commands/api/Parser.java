package com.ziluck.fortuneblocks.commands.api;

import java.util.Iterator;
import java.util.List;

import org.bukkit.command.CommandSender;

public interface Parser<T> {
    /**
     * Parses the argument from the raw string into the appropriate type. If the argument can't be parsed
     *
     * @param sender      the sender of the command.
     * @param label       the label of the command
     * @param rawArgument the argument to be parsed.
     * @return the successfully parsed argument.
     */
    T parseArgument(CommandSender sender, String[] label, String rawArgument);

    /**
     * Get tab complete recommendations for an argument with this given parser. If the default is wanted, it exists in
     * {@link CommandHandler#defaultTabComplete(CommandSender, String)}.
     *
     * @param sender   the sender of the tab complete.
     * @param lastWord the content of the item so far.
     * @return the recommendations.
     */
    List<String> getRecommendations(CommandSender sender, String lastWord);

    static void pruneSuggestions(List<String> values, String lastWord) {
        if (values == null || values.isEmpty()) {
            return;
        }
        lastWord = lastWord.toLowerCase();
        Iterator<String> it = values.iterator();
        while (it.hasNext()) {
            if (!it.next().toLowerCase().startsWith(lastWord)) {
                it.remove();
            }
        }
    }

}
