package com.axolotl.yanews.dagger;

import com.axolotl.yanews.fragment.PageFragment;

import dagger.Component;

/**
 * Created by axolotl on 16/9/24.
 */
@ActivityScope
@Component(dependencies = MainComponent.class, modules = PageFraModule.class)
public interface PageComponent {
    void inject(PageFragment fragment);
}
