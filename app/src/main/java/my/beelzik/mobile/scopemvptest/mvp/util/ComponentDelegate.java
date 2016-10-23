package my.beelzik.mobile.scopemvptest.mvp.util;

/**
 * Created by Andrey on 23.10.2016.
 */

public class ComponentDelegate<T> {

    private final String mTag;

    private T mComponent;
    private IHasComponent<T> mHasComponent;

    public ComponentDelegate(String tag, IHasComponent<T> hasComponent) {
        mTag = tag;
        mHasComponent = hasComponent;
    }

    public T getComponent() {

        if (mComponent == null) {
            if (ComponentCache.hasComponent(mTag)) {
                mComponent = (T) ComponentCache.getComponent(mTag);
            } else {
                mComponent = mHasComponent.createComponent();
                ComponentCache.addComponent(mTag, mComponent);
            }
        }

        return mComponent;
    }

    public void removeComponent() {
        mComponent = null;
        ComponentCache.removeComponent(mTag);
    }
}
