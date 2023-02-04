package ru.vkbot;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.messages.*;
import com.vk.api.sdk.queries.messages.MessagesGetLongPollHistoryQuery;

public class Bot {
    public static void main(String[] args) throws ClientException, ApiException, InterruptedException
    {
        TransportClient transportClient = new HttpTransportClient();
        VkApiClient vk = new VkApiClient(transportClient);
        Random random = new Random();
        Keyboard keyboard = new Keyboard();

        List<List<KeyboardButton>> allKey = new ArrayList<>();
        List<KeyboardButton> line1 = new ArrayList<>();
        line1.add(new KeyboardButton().setAction(new KeyboardButtonAction().setLabel("Форма").setType(KeyboardButtonActionType.TEXT)).setColor(KeyboardButtonColor.POSITIVE));
        line1.add(new KeyboardButton().setAction(new KeyboardButtonAction().setLabel("Консультант").setType(KeyboardButtonActionType.TEXT)).setColor(KeyboardButtonColor.POSITIVE));
        line1.add(new KeyboardButton().setAction(new KeyboardButtonAction().setLabel("Цены").setType(KeyboardButtonActionType.TEXT)).setColor(KeyboardButtonColor.POSITIVE));
        allKey.add(line1);
        keyboard.setButtons(allKey);
        GroupActor actor = new GroupActor( 218629979,"vk1.a.t7YEJ75UVDvEKCiacdDG0Di4y5FFWqMByJk1qQTaue8yWYTqzLlvddfkyWhzYXS_nsOcOKCFpIFIYeg7VmbuJsygJ6ipGDDfFgkWbsSMPErQ5q4rjbslJCnX2YHeYwaf8aRKZNNa8pO19ropQeZFW_a9hB-Pcpc71Ae7wCfJFDA_zLWxS5DEPzR3DBGHAX0pz82v0ox5QIdZRDtkAuhtjw   ");
        Integer ts = vk.messages().getLongPollServer(actor).execute().getTs();
        int stage1 = 0;

        while (true) {
            MessagesGetLongPollHistoryQuery historyQuery = vk.messages().getLongPollHistory(actor).ts(ts);
            List<Message> messages = historyQuery.execute().getMessages().getItems();
            if (!messages.isEmpty()) {
                for (Message message : messages) {
                    try {

                        if (message.getText().equals("Форма")) {
                            String str = "Заполните форму по шаблону! Отвечайте да или нет в сободное время вас проконсультируют\n" +
                                    "1. Вы выбрали место для тату?: да/нет";
//                                    "2. Вы выбрали размер татуировки?: нет\n" +
//                                    "3. Вы выбрали тату которое хотите набить?: нет\n" +
//                                    "4. Вам нужна помощь с выбором эскиза?: да\n" +
//                                    "5. На какую сумму расчистывете?";
                            stage1 = 0;
                            vk.messages().send(actor).message(str).userId(message.getFromId()).randomId(random.nextInt(10000)).execute();
                        } else if (message.getText().equals("Консультант")) {
                            stage1 = 0;
                            String str = "Рекоммендуется сначала заполнить форму, для более быстрого решения ваших вопросов. Консультант ответит вам в период 15:00 - 17:00";
                            vk.messages().send(actor).message(str).userId(message.getFromId()).randomId(random.nextInt(10000)).execute();
                        } else  if (message.getText().equals("Цены")) {
                            stage1 = 0;
                            String str = "Маленькое тату. Размер 5х5: от 3000\n" +
                                    "среднее тату. Размер 10х10: от 5000\n" +
                                    "большое тату. Размер 10х10: от 5000\n";
                            vk.messages().send(actor).message(str).userId(message.getFromId()).randomId(random.nextInt(10000)).execute();
                        } else if (message.getText().equals("Кнопки")) {
                            stage1 = 0;
                            vk.messages().send(actor).message("А вот и они").userId(message.getFromId()).randomId(random.nextInt(10000)).keyboard(keyboard).execute();
                        } else if ((message.getText().toLowerCase().equals("да") || message.getText().toLowerCase().equals("нет")) && stage1 == 0) {
                            String str = "2. Вы выбрали размер татуировки?: да/нет\n";
//
                            stage1 = 1;
                            vk.messages().send(actor).message(str).userId(message.getFromId()).randomId(random.nextInt(10000)).execute();
                        } else if ((message.getText().toLowerCase().equals("да") || message.getText().toLowerCase().equals("нет")) && stage1 == 1) {
                        String str = "3. Вы выбрали тату которое хотите набить?: да/нет\n";
//                                    "4. Вам нужна помощь с выбором эскиза?: да\n" +
//                                    "5. На какую сумму расчистывете?";
                            stage1 = 2;
                            vk.messages().send(actor).message(str).userId(message.getFromId()).randomId(random.nextInt(10000)).execute();
                        } else if (message.getText().toLowerCase().equals("да") && stage1 == 2) {
                            String str = "4. Пришлите изображение\n";
//                                    "5. На какую сумму расчистывете?";
                            stage1 = 3;
                            vk.messages().send(actor).message(str).userId(message.getFromId()).randomId(random.nextInt(10000)).execute();
                        } else if (message.getText().toLowerCase().equals("нет") && stage1 == 2) {
                            String str = "4. Вам помочь с выбором эскиза? да/нет\n";
//                                    "5. На какую сумму расчистывете?";
                            stage1 = 3;
                            vk.messages().send(actor).message(str).userId(message.getFromId()).randomId(random.nextInt(10000)).execute();
                        } else if (stage1 == 3) {
                            String str = "5. На какую сумму расчистывете?\n";
                            stage1 = 4;
                            vk.messages().send(actor).message(str).userId(message.getFromId()).randomId(random.nextInt(10000)).execute();
                        } else if (stage1 == 4) {
                            String str = "Благодарим за заполнение формы. Консультант свяжеться свами в периоде с 15:00 до 17:00";
                            vk.messages().send(actor).message(str).userId(message.getFromId()).randomId(random.nextInt(10000)).execute();
                        }
                        else {
                            vk.messages().send(actor).message("Неверная команда!").userId(message.getFromId()).randomId(random.nextInt(10000)).keyboard(keyboard).execute();
                        }
                    } catch (ApiException | ClientException e) {e.printStackTrace();}
                }
            }
            ts = vk.messages().getLongPollServer(actor).execute().getTs();
            Thread.sleep(500);
        }
    }
}
