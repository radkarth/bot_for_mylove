package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.*;

public class MyFirstTelegramBot extends TelegramLongPollingBot {

    // ==== ВСТАВЬ НИЖЕ ДАННЫЕ БОТА ====
    private static final String BOT_USERNAME = "for_my_vilia_bot";     // <- сюда ник бота
    private static final String BOT_TOKEN = "8597915206:AAGBAx3DQfzDV36jiiAs-v6mVu0AMmXaXOg";      // <- сюда токен
    // ================================

    private final Map<Long, Integer> love = new HashMap<>();
    private final Map<Long, Integer> scene = new HashMap<>();
    private final Set<Long> secretOnce = new HashSet<>();

    // ---- картинки ----
    private static final String IMG_BEGIN = "https://i.pinimg.com/1200x/e5/8c/9d/e58c9d8a8589ed51c4980b41fdf6c109.jpg";
    private static final String IMG_START = "https://i.pinimg.com/736x/02/b5/6b/02b56be600c4dbcf8d363d98cac634a0.jpg";
    private static final String IMG_FIRST = "https://i.pinimg.com/736x/5e/ce/b6/5eceb618c2367cd0d3941037b18feb0d.jpg";
    private static final String IMG_SECOND = "https://i.pinimg.com/1200x/81/71/51/8171515f162cc1254da32d59c23ad43f.jpg";
    private static final String IMG_CONF = "https://i.pinimg.com/1200x/2d/7a/2a/2d7a2a4cdde8a2be82674316c0afc0aa.jpg";
    private static final String IMG_WALK = "https://i.pinimg.com/736x/6b/ef/50/6bef50f0cba8ce585c1d7073e5e0cf4d.jpg";
    private static final String IMG_SECRET = "https://i.pinimg.com/736x/c8/7f/c1/c87fc1748f3ad3b678df07b71223e186.jpg";
    private static final String IMG_FINAL = "https://i.pinimg.com/736x/45/87/4a/45874a1bb0774d9015c88ca7cb47611f.jpg";
    private static final String IMG_1 = "https://i.pinimg.com/736x/f9/d2/9f/f9d29f504dcbd8fad5dd5b8cad4d1227.jpg";
    private static final String IMG_2 = "https://i.pinimg.com/736x/0b/0d/f7/0b0df7f5f03a9885c74b1d30ec9fbbb5.jpg";
    private static final String IMG_3 = "https://i.pinimg.com/736x/70/5e/6a/705e6af367631f567eb0a91195805f3d.jpg";
    private static final String IMG_4 = "https://i.pinimg.com/736x/49/ee/9c/49ee9ce073666a97e700513c198c2bac.jpg";
    private static final String IMG_5 = "https://i.pinimg.com/1200x/11/c5/c4/11c5c410f039789fc2626f6ec20be895.jpg";
    @Override
    public String getBotUsername() { return BOT_USERNAME; }
    @Override
    public String getBotToken() { return BOT_TOKEN; }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) return;

        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();

        love.putIfAbsent(chatId, 0);
        scene.putIfAbsent(chatId, 0);

        int sc = scene.get(chatId);

        // логика сцен
        if (sc == 1) { firstChoice(chatId, text); return; }
        if (sc == 3) { secondChoice(chatId, text); return; }
        if (sc == 5) { confessionChoice(chatId, text); return; }

        switch (text) {
            case "/start" -> start(chatId);
            case "📖 Продолжить" -> continueStory(chatId);
            case "🔒 Секрет (один раз)" -> oneTimeSecret(chatId);
            default -> sendPhoto(chatId, IMG_START, "Я ЛЮБЛЮ ТЕБЯ, КОТЕНОЧЕЕЕЕЕЕЕК!!!!!!💕", mainKb());
        }
    }

    private void start(Long chatId) {
        love.put(chatId, 0);
        scene.put(chatId, 0);

        sendPhoto(chatId, IMG_BEGIN,
                "Привет, моя любимая девочка 💖\n" +
                        "С Новым Годом тебя, котёнок!!!✨\n" +
                        "Я написал небольшого бота — надеюсь, тебе понравится 😊",
                mainKb());
    }

    private void continueStory(Long chatId) {
        int sc = scene.get(chatId);

        if (sc == 0) {
            scene.put(chatId, 1);
            sendPhoto(chatId, IMG_FIRST,
                    "Тёплый вечер… Мы рядом 🌙\nЧего-то не хватает…",
                    firstChoiceKb());
        } else if (sc == 2) {
            scene.put(chatId, 3);
            sendPhoto(chatId, IMG_SECOND,
                    "Между нами возникает тихое напряжение…",
                    secondChoiceKb());
        } else if (sc == 4) {
            scene.put(chatId, 5);
            sendPhoto(chatId, IMG_CONF,
                    "Я смотрю на тебя… И решаюсь… Как отреагируешь? 💞",
                    confessionKb());
        }
    }

    private void firstChoice(Long chatId, String c) {
        switch (c) {
            case "🤍 Приблизиться" -> love.merge(chatId, 2, Integer::sum);
            case "😊 Пошутить" -> love.merge(chatId, 1, Integer::sum);
            case "🙈 Смущаться" -> {}
            default -> { sendPhoto(chatId, IMG_1, "Выбери вариант 💫", firstChoiceKb()); return; }
        }
        scene.put(chatId, 2);
        sendPhoto(chatId, IMG_WALK, "Мне так тепло рядом с тобой... 🤍\n❤️ " + love.get(chatId), mainKb());
    }

    private void secondChoice(Long chatId, String c) {
        switch (c) {
            case "💖 Взять за руку" -> love.merge(chatId, 3, Integer::sum);
            case "😌 Посмотреть в глаза" -> love.merge(chatId, 2, Integer::sum);
            case "🌸 Улыбнуться" -> love.merge(chatId, 1, Integer::sum);
            default -> { sendPhoto(chatId, IMG_2, "Выбери вариант 💫", secondChoiceKb()); return; }
        }
        scene.put(chatId, 4);
        sendPhoto(chatId, IMG_3, "Я укутываю твои руки в свои ✨\n❤️ " + love.get(chatId), mainKb());
    }

    private void confessionChoice(Long chatId, String c) {
        switch (c) {
            case "💗 Подойти ближе" -> love.merge(chatId, 2, Integer::sum);
            case "😊 Улыбнуться" -> love.merge(chatId, 1, Integer::sum);
            case "🙈 Смущённо опустить глаза" -> {}
            default -> { sendPhoto(chatId, IMG_5, "Выбери реакцию 💫", confessionKb()); return; }
        }
        finale(chatId);
    }

    private void finale(Long chatId) {
        int p = love.get(chatId);
        String text;

        if (p >= 10) {
            text = "Ты стала частичкой меня 💍\n" +
                    "Хочу писать историю с тобой до конца 💖";
        } else if (p >= 6) {
            text = "Этот вечер стал особенным 🌙\n" +
                    "С тобой хочется быть ближе 💖";
        } else {
            text = "Мне было тепло рядом с тобой 🌸";
        }

        scene.put(chatId, 0);
        sendPhoto(chatId, IMG_FINAL, text + "\n\n❤️ Итог: " + p, mainKb());
    }

    private void oneTimeSecret(Long chatId) {
        if (secretOnce.contains(chatId) || love.get(chatId) < 10) {
            sendPhoto(chatId, IMG_4, "Секрет недоступен 🤍", mainKb());
            return;
        }

        secretOnce.add(chatId);

        sendPhoto(chatId, IMG_SECRET,
                """
        Любимая моя, я так рад что ты у меня есть.
        Я довольствуюсь этому каждый день.
        С того самого дня, с той самой первой игры , когда мы познакомились)
        Я люблю тебя, обожаю тебя, хочу быть с тобой каждую секунду, каждую минуту...
        Когда тебе весело, когда тебе грустно, когда одиноко.
        В тот день я и подумать не мог, что у нас что-то получится)
        До сих пор не верится, что мы познакомились в игре хехех.
        Где большое количество людей, и... случилось такое немыслимое совпадение)
        Я хочу быть с тобой, хочу жить с тобой, хочу жить тобою.
        Но мое самое большое желание — быть твоим любимым мужем.
        Твоей опорой, твоим прикрытием, твоим другом, твоим любимым)
        С Новым Годом, моя прелесть!!!!❤️❤️❤️❤️❤️❤️❤️❤️❤️💍💍💍💍💍
                """,
                mainKb());
    }

    // === отправка фото ===
    private void sendPhoto(Long chatId, String url, String text, ReplyKeyboardMarkup kb) {
        SendPhoto sp = new SendPhoto();
        sp.setChatId(chatId.toString());
        sp.setPhoto(new InputFile(url));
        sp.setCaption(text);
        sp.setReplyMarkup(kb);

        try { execute(sp); }
        catch (TelegramApiException e) { e.printStackTrace(); }
    }

    // === клавиатуры ===
    private ReplyKeyboardMarkup mainKb() {
        KeyboardRow r = new KeyboardRow();
        r.add("📖 Продолжить");
        r.add("🔒 Секрет (один раз)");
        return kb(r);
    }

    private ReplyKeyboardMarkup firstChoiceKb() {
        KeyboardRow r1 = new KeyboardRow();
        r1.add("🤍 Приблизиться");
        r1.add("😊 Пошутить");
        KeyboardRow r2 = new KeyboardRow();
        r2.add("🙈 Смущаться");
        return kb(r1, r2);
    }

    private ReplyKeyboardMarkup secondChoiceKb() {
        KeyboardRow r1 = new KeyboardRow();
        r1.add("💖 Взять за руку");
        r1.add("😌 Посмотреть в глаза");
        KeyboardRow r2 = new KeyboardRow();
        r2.add("🌸 Улыбнуться");
        return kb(r1, r2);
    }

    private ReplyKeyboardMarkup confessionKb() {
        KeyboardRow r1 = new KeyboardRow();
        r1.add("💗 Подойти ближе");
        r1.add("😊 Улыбнуться");
        KeyboardRow r2 = new KeyboardRow();
        r2.add("🙈 Смущённо опустить глаза");
        return kb(r1, r2);
    }

    private ReplyKeyboardMarkup kb(KeyboardRow... rows) {
        ReplyKeyboardMarkup k = new ReplyKeyboardMarkup();
        k.setKeyboard(Arrays.asList(rows));
        k.setResizeKeyboard(true);
        return k;
    }

    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(new MyFirstTelegramBot());
    }
}
