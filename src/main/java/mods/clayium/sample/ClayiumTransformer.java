package mods.clayium.sample;

import cpw.mods.fml.relauncher.FMLLaunchHandler;
import cpw.mods.fml.relauncher.Side;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;


public class ClayiumTransformer
        implements IClassTransformer, Opcodes {
    private static final String TARGET_CLASS_NAME = "net.minecraft.src.TargetClass";

    public byte[] transform(String name, String transformedName, byte[] bytes) {
        if (FMLLaunchHandler.side() != Side.CLIENT || !name.equals("net.minecraft.src.TargetClass")) ;


        try {
            if (name.equals("net.minecraft.client.renderer.WorldRenderer")) {
                return replaceClass(bytes, "net\\minecraft\\client\\renderer\\WorldRenderer.class");
            }
            if (name.equals("net.minecraft.client.renderer.EntityRenderer")) {
                return replaceClass(bytes, "net\\minecraft\\client\\renderer\\EntityRenderer.class");
            }
            if (name.equals("net.minecraft.client.renderer.RenderGlobal")) {
                return replaceClass(bytes, "net\\minecraft\\client\\renderer\\RenderGlobal.class");
            }
            return bytes;

        } catch (Exception e) {

            throw new RuntimeException("failed : TutorialTransformer loading", e);
        }
    }


    private byte[] replaceClass(byte[] bytes, String targetClass) throws IOException {
        ZipFile zf = null;
        InputStream zi = null;


        try {
            zf = new ZipFile(ClayiumTransformerLoadingPlugin.location);


            ZipEntry ze = zf.getEntry(targetClass);

            if (ze != null) {

                zi = zf.getInputStream(ze);
                int len = (int) ze.getSize();
                bytes = new byte[len];


                int MAX_READ = 1024;
                int readed = 0;
                while (readed < len) {
                    int readsize = MAX_READ;
                    if (len - readed < MAX_READ) {
                        readsize = len - readed;
                    }
                    int ret = zi.read(bytes, readed, readsize);
                    if (ret == -1)
                        break;
                    readed += ret;
                }
            }

            return bytes;
        } catch (Exception e) {
            zi = new FileInputStream(ClayiumTransformerLoadingPlugin.location + "\\" + targetClass);
            int len = zi.available();

            if (zi != null) {


                bytes = new byte[len];


                int MAX_READ = 1024;
                int readed = 0;
                while (readed < len) {
                    int readsize = MAX_READ;
                    if (len - readed < MAX_READ) {
                        readsize = len - readed;
                    }
                    int ret = zi.read(bytes, readed, readsize);
                    if (ret == -1)
                        break;
                    readed += ret;
                }
                zi.close();
                return bytes;
            }
        } finally {

            if (zi != null) {
                zi.close();
            }

            if (zf != null) {
                zf.close();
            }
        }
        return bytes;
    }


    private byte[] hookDoRenderLivingMethod(byte[] bytes) {
        ClassNode cnode = new ClassNode();
        ClassReader reader = new ClassReader(bytes);
        reader.accept((ClassVisitor) cnode, 0);


        String targetMethodName = "doRenderLiving";


        String targetMethoddesc = "(Lnet/minecraft/entity/EntityLiving;DDDFF)V";


        MethodNode mnode = null;
        for (MethodNode curMnode : cnode.methods) {

            if (targetMethodName.equals(curMnode.name) && targetMethoddesc.equals(curMnode.desc)) {

                mnode = curMnode;

                break;
            }
        }
        if (mnode != null) {

            InsnList overrideList = new InsnList();


            overrideList.add((AbstractInsnNode) new VarInsnNode(25, 1));
            overrideList.add((AbstractInsnNode) new VarInsnNode(24, 2));
            overrideList.add((AbstractInsnNode) new VarInsnNode(24, 4));
            overrideList.add((AbstractInsnNode) new VarInsnNode(24, 6));
            overrideList
                    .add((AbstractInsnNode) new MethodInsnNode(184, "tutorial/test", "passTestRender", "(LEntityLiving;DDD)V"));


            mnode.instructions.insert(mnode.instructions.get(1), overrideList);


            ClassWriter cw = new ClassWriter(3);
            cnode.accept(cw);
            bytes = cw.toByteArray();
        }

        return bytes;
    }
}
