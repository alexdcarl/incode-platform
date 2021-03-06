package org.incode.module.slack.impl;

import java.util.Map;
import java.util.Optional;

import com.google.common.collect.Maps;
import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;

class ChannelCache {

    private final SlackSession slackSession;
    private final Map<String, Optional<SlackChannel>> channelsByName = Maps.newConcurrentMap();

    ChannelCache(final SlackSession slackSession) {
        this.slackSession = slackSession;
    }

    SlackChannel findChannel(final String channelName) {
        Optional<SlackChannel> slackChannelIfAny = channelsByName.get(channelName);
        if(slackChannelIfAny == null) {
            slackChannelIfAny = Optional.ofNullable(slackSession.findChannelByName(channelName));
            channelsByName.put(channelName, slackChannelIfAny);
        }
        return slackChannelIfAny.orElse(null);
    }

}
