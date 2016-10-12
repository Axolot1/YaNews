package com.axolotl.yanews.dagger;

import com.axolotl.yanews.AddCommentActivity;
import com.axolotl.yanews.DetailActivity;
import com.axolotl.yanews.MainActivity;
import com.axolotl.yanews.SignInActivity;
import com.axolotl.yanews.fragment.CommentFragment;
import com.axolotl.yanews.model.NewsModel;
import com.axolotl.yanews.service.DownLoadService;
import com.axolotl.yanews.service.NewsBackupService;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by axolotl on 16/6/29.
 */
@Singleton
@Component(modules = {AppModule.class, NetModule.class})
public interface MainComponent {

    void inject(CommentFragment fragment);

    void inject(SignInActivity activity);

    void inject(DetailActivity activity);

    void inject(AddCommentActivity addCommentActivity);

    void inject(DownLoadService service);

    void inject(MainActivity activity);

    void inject(NewsBackupService backupService);

    NewsModel model();
}
