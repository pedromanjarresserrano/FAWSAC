package com.gitlab.pedrioko.providers;

import com.gitlab.pedrioko.core.view.api.GroupProvider;
import com.gitlab.pedrioko.core.view.api.MenuProvider;
import com.gitlab.pedrioko.core.view.api.Provider;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AdminGroupProvider implements GroupProvider {

    private List<Provider> content;

    @PostConstruct
    private void init() {
        this.content = ApplicationContextUtils.getBeans(MenuProvider.class).stream().filter(e -> e.getGroup() != null && e.getGroup().equals(this.getClass())).collect(Collectors.toList());
    }

    @Override
    public List<Provider> getContent() {
        return content;
    }

    @Override
    public String getLabel() {
        return "Administración";
    }

    @Override
    public String getIcon() {
        return null;
    }

    @Override
    public int getPosition() {
        return 100;
    }
}
