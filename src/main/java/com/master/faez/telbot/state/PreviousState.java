package com.master.faez.telbot.state;

import com.master.faez.telbot.core.MessageProcessor;
import com.master.faez.telbot.core.UserSession;
import com.master.faez.telbot.models.User;
import com.master.faez.telbot.services.UserStateServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;

@Service
public class PreviousState {
    @Autowired
    MessageProcessor messageProcessor;
    @Autowired
    UserStateServiceImp userStateServiceImp;
    @EventListener(PreviousStateEvent.class)
    public void onPreviousState(PreviousStateEvent event) {
        UserSession userSession = event.getUserSession();
        userStateServiceImp.previousStep(userSession);
        messageProcessor.processMessage(userSession.getUpdate());
    }

}
