package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.sound.midi.Soundbank;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Bot extends TelegramLongPollingBot {

    final private String BOT_NAME = "aidamirpervi_bot";//aidamirpervi_bot
    final private String BOT_TOKEN = "6644984596:AAGVc9_d76VZQ9jNJK9M7ZdpYM6F62D7lso";//6644984596:AAGVc9_d76VZQ9jNJK9M7ZdpYM6F62D7lso
    private DB db;
    private ArrayList<Long> arrayList;

    public Bot() {
        dataBase();
    }

    public void dataBase(){
        db = new DB();
        db.open();
        db.createTable();
    }

    ArrayList<String> userName = new ArrayList<String>(
            Arrays.asList("firefoxfi","Mypervie01ra","Alesya_Merinova","olika_bliznyk", "nasya_a11", "id3axaoxa", "timur24khut", "shekeryants"));
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() ) {
            Message inMess = update.getMessage();
            Long currChatId = inMess.getChatId();
            Chat inChat = inMess.getChat();
//            long l = -1001810698931_1L;
//            System.out.println(l);
            System.out.println(currChatId);
            System.out.println( inMess.getFrom().getUserName());
            if(userName.contains(inMess.getFrom().getUserName())) {
                if (currChatId < 0) {
                    if (!db.checkChat(currChatId)) {
                        db.insertTable(inChat.getTitle(), currChatId);
                    }
                } else if (currChatId > 0 && (userName.contains(inChat.getUserName()))) {
                    arrayList = db.getChatList();
                    if (inMess.isCommand()) {
                        sendMessage(currChatId, parseMessage(inMess.getText()));
                    } else {
                        for (long chatId : arrayList) {
                            if (inMess.hasText()) {
//                            String parseMode = userParseModeMap.getOrDefault(chatId,ParseMode.MARKDOWN);
                                sendMessage(chatId, inMess.getText());
                            } else if (inMess.hasPhoto()) {
                                sendPhoto(chatId, inMess);
                            } else if (inMess.hasVideo()) {
                                sendVideo(chatId, inMess);
                            } else if (inMess.hasVoice()) {
                                sendVoice(chatId, inMess);
                            } else if (inMess.hasVideoNote()) {
                                sendVideoNote(chatId, inMess);
                            }
                        }
                    }
                }
            }
            else{
                System.out.println("У пользователя недостаточно прав");
            }
        }
    }


    public void sendMessage(long chatId, String str){
        try {
//            String response = parseMessage(str);

            SendMessage outMess = new SendMessage();
            outMess.setChatId(chatId);
            outMess.setParseMode(ParseMode.MARKDOWN);
            outMess.setText(str);
            execute(outMess);
        } catch (TelegramApiException e) {
            System.out.println("Чат не найден");
//            db.deleteOnId(chatId);
        }
    }

    public void sendPhoto(long chatId, Message inMess){
        try {
            SendPhoto outPhoto = new SendPhoto();
            if (inMess.getCaption() != null) outPhoto.setCaption(inMess.getCaption());
            outPhoto.setChatId(chatId);

            outPhoto.setPhoto(new InputFile(inMess.getPhoto().get(0).getFileId()));
            execute(outPhoto);
        } catch (TelegramApiException e) {
            System.out.println("Чат не найден");
//            db.deleteOnId(chatId);
        }
    }

    public void sendVoice(long chatId, Message inMess){
        try {
            SendVoice outVoice = new SendVoice();
            outVoice.setChatId(chatId);
            outVoice.setVoice(new InputFile(inMess.getVoice().getFileId()));
            execute(outVoice);
        } catch (TelegramApiException e) {
            System.out.println("Чат не найден");
//            db.deleteOnId(chatId);
        }
    }

    public void sendVideo(long chatId, Message inMess){
        try {
            SendVideo outVideo = new SendVideo();
            if (inMess.getCaption() != null) outVideo.setCaption(inMess.getCaption());

            outVideo.setChatId(chatId);

            outVideo.setVideo(new InputFile(inMess.getVideo().getFileId()));
            execute(outVideo);
        } catch (TelegramApiException e) {
            System.out.println("Чат не найден");
//            db.deleteOnId(chatId);
        }
    }

    public void sendVideoNote(long chatId, Message inMess){
        try {
            SendVideoNote outVideo = new SendVideoNote();

            outVideo.setChatId(chatId);

            outVideo.setVideoNote(new InputFile(inMess.getVideoNote().getFileId()));
            execute(outVideo);
        } catch (TelegramApiException e) {
            System.out.println("Чат не найден");
//            db.deleteOnId(chatId);
        }
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    public String parseMessage(String textMsg) {
        String response;

        //Сравниваем текст пользователя с нашими командами, на основе этого формируем ответ
        if(textMsg.equals("/start")) {
            response = "Привет, я Бот для рассылки сообщений. \nНапиши, какое сообщение хочешь разослать по всем группам";
            arrayList = db.getChatList();
        } else
            response = "Простите, но я таких команд не знаю =[";

        return response;
    }

    public String getBotToken() {
        return BOT_TOKEN;
    }

}
