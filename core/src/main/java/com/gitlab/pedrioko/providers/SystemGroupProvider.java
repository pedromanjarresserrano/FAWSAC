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
public class SystemGroupProvider implements GroupProvider {

    private List<Provider> content;

    private List<Provider> init() {
        this.content = ApplicationContextUtils.getBeans(MenuProvider.class)
                .stream()
                .filter(e -> e.getGroup() != null && e.getGroup().equals(this.getClass()))
                .collect(Collectors.toList());

        return this.content;
    }

    @Override
    public List<Provider> getContent() {
        return content == null ? init() : content;
    }

    @Override
    public String getName() {
        return "sys-menu";
    }

    @Override
    public String getLabel() {
        return "System";
    }

    @Override
    public String getIcon() {
        return null;
    }

    @Override
    public int getPosition() {
        return 101;
    }
}
