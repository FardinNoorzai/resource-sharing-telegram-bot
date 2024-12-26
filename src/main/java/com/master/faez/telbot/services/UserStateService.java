package com.master.faez.telbot.services;


import com.master.faez.telbot.core.UserSession;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface UserStateService {
    public UserSession getCurrentSession(Update update);
}
