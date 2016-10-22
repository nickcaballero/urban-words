package com.idklabs.alexa.amzn;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.json.SpeechletResponseEnvelope;
import com.amazon.speech.speechlet.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.lang.annotation.*;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @author nick.caballero
 */
public class AbstractSpeechlet implements Speechlet {

    private final Map<String, Method> intents = Maps.newHashMap();
    private final SpeechletRequestDispatcher dispatcher = new SpeechletRequestDispatcher(this);

    public AbstractSpeechlet() {
        Method[] declaredMethods = getClass().getDeclaredMethods();
        for (Method method : declaredMethods) {
            if (method.isAnnotationPresent(Intent.class)) {
                intents.put(method.getAnnotation(Intent.class)
                                  .value(),
                                method);
            }
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public SpeechletResponseEnvelope handleRequest(@RequestBody SpeechletRequestEnvelope request)
                    throws IOException, SpeechletRequestHandlerException, SpeechletException {
        return dispatcher.dispatchSpeechletCall(request, request.getSession());
    }

    @Override
    public void onSessionStarted(SessionStartedRequest request, Session session)
                    throws SpeechletException {}

    @Override
    public SpeechletResponse onLaunch(LaunchRequest request, Session session)
                    throws SpeechletException {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpeechletResponse onIntent(IntentRequest request, Session session)
                    throws SpeechletException {
        String name = request.getIntent()
                             .getName();
        Method method = intents.get(name);
        try {
            List<Object> parameters = Lists.newArrayList();

            for (Class<?> type : method.getParameterTypes()) {
                if (type == IntentRequest.class) {
                    parameters.add(request);
                }
                if (type == Session.class) {
                    parameters.add(session);
                }
            }

            return (SpeechletResponse) method.invoke(this, parameters.toArray());
        } catch (Exception e) {
            throw new SpeechletException(e);
        }
    }

    @Override
    public void onSessionEnded(SessionEndedRequest request, Session session)
                    throws SpeechletException {}

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface Intent {
        String value();
    }
}
