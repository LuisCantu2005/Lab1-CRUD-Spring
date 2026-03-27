package com.springboot.MyTodoList.util;

import org.telegram.telegrambots.meta.generics.TelegramClient;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.springboot.MyTodoList.config.BotProps;


@Configuration
@ConditionalOnProperty(name = "telegram.bot.enabled", havingValue = "true", matchIfMissing = false)
public class BotClient {

    @Bean
    public TelegramClient telegramClient(BotProps botProps) {
        return new OkHttpTelegramClient(botProps.getToken());
    }
}