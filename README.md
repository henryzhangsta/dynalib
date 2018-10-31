# DynaNode - CrossJVM Code Injection

Designed for use in RuneMate, DynaNode provides the capability to modify bytecode at runtime.

### How does it work?

It installs a java agent in the target JVM and sets up a RMI service that will receive injected classes and hot-swap them into the running environment

### In the Context of Runescape
In OldSchool RuneScape once the gamepack is loaded the class files cannot be found on the classpath AFAIK. 
To combat this I implemented a simple CDN that serves the raw class files from the current revision.
So by querrying: `https://updates.paratek.io/pack/class/:classname` you will receive a raw unmodifed class from the current revision.
You can then inject code into this class file with ASM using the helper functions in `AbstractTransformer` you then write it to a byte array
and send it with `AbstractTransformer#writeToBridge`. Examples of this can be found in the `impl` package of `dynalib`.

### How to use it
##### Basic Example
```java
final DynaLoader loader = new DynaLoader(VM_ID);
loader.submitTransformer(new AbstractTransformer(...));
loader.init();
```
##### Full RuneMate Example
The following code will disable model rendering when the bot is paused, and enable model rendering when it's resumed
```java
import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.hybrid.input.Mouse;
import com.runemate.game.api.script.framework.LoopingBot;
import io.paratek.dynanode.DynaBridge;
import io.paratek.dynanode.DynaLoader;
import io.paratek.dynanode.transformers.impl.ActionTransformer;
import io.paratek.dynanode.transformers.impl.ModelRenderingTransformer;

import java.rmi.RemoteException;

public class DynaBot extends LoopingBot {

    private final DynaLoader loader;

    public DynaBot() {
        this.loader = new DynaLoader(Environment.getRuneScapeProcessId());
        this.loader.submitTransformer(new ModelRenderingTransformer());
        this.loader.submitTransformer(new ActionTransformer());
        this.loader.init();
    }

    @Override
    public void onStart(String... arguments) {
        Mouse.setPathGenerator(Mouse.HOPPING_PATH_GENERATOR);
    }

    public void onLoop() {
        System.out.println("LOOP");
    }

    @Override
    public void onResume() {
        if (this.getBridge() != null) {
            try {
                this.getBridge().setRenderingModels(false);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onPause() {
        if (this.getBridge() != null) {
            try {
                this.getBridge().setRenderingModels(true);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private DynaBridge getBridge() {
        if (this.loader != null) {
            return this.loader.getBridge();
        }
        return null;
    }
    
}
```

#### Here is a visualization of the above code

![alt text](https://i.imgur.com/oGhV9xS.gif)
