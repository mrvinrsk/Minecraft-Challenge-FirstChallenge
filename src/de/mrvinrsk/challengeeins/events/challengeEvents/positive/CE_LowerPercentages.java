package de.mrvinrsk.challengeeins.events.challengeEvents.positive;

import de.chatvergehen.spigotapi.util.random.RandomNumber;
import de.mrvinrsk.challengebase.util.*;
import de.mrvinrsk.challengeeins.main.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class CE_LowerPercentages implements PercentageChallengeEvent {

    private ChallengeEventManager eventManager = ChallengeEventManager.getManager();
    private Gameplay gameplay = Gameplay.getInstance();
    private double lower = .5;

    @Override
    public double getBasePercentage() {
        return 10.;
    }

    @Override
    public String getEventName() {
        return "Ich angel mir bessere Chancen";
    }

    @Override
    public String getConfigName() {
        return "fishing_percentages";
    }

    @Override
    public List<String> getDescription(Player player) {
        return Arrays.asList(
                "Angeln kann dafür sorgen, dass sich die",
                "Wahrscheinlichkeit für ein zufälliges negatives Event",
                "für dich um " + lower + "% senkt."
        );
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.FISHING_ROD);
    }

    @Override
    public ChallengeEventType getType() {
        return ChallengeEventType.POSITIVE;
    }

    @EventHandler
    public void fish(PlayerFishEvent e) {
        Player p = e.getPlayer();

        if (e.getCaught() != null) {
            if (RandomNumber.rnddouble(1., 100.) <= eventManager.getPercentage(p, this)) {
                List<PercentageChallengeEvent> events = eventManager.getPercentageEvents();
                events.removeIf(ev -> ev.getType() != ChallengeEventType.NEGATIVE); // REMOVE WHEN NOT NEGATIVE

                if (events.size() >= 1) {
                    PercentageChallengeEvent event = events.get(RandomNumber.rndinteger(0, events.size() - 1));
                    String evtName = "";

                    if (!eventManager.achieved(event)) {
                        StringBuilder obfName = new StringBuilder();
                        for (int i = 0; i < event.getEventName().length(); i++) {
                            obfName.append("0");
                        }
                        evtName = obfName.toString();
                    } else {
                        evtName = event.getEventName();
                    }

                    double pc = eventManager.getPercentage(p, event);
                    if (pc >= lower) {
                        eventManager.setPercentage(p, event, pc - lower);

                        eventManager.triggerEvent(p, this, Main.getPlugin());
                        gameplay.sendMessage(p, GameplayMessageType.SYSTEM, "Die Wahrscheinlichkeit das Event §a§k" + evtName + " §rauszulösen hat sich für dich um §e" + lower + "% §rgesenkt.");
                    }
                } else {
                    System.out.println("Es gibt noch keine negativen Events in dieser Challenge.");
                }
            }
        }
    }

}
