package mods.clayium.asm;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import net.minecraft.launchwrapper.IClassTransformer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;


public class ClayiumTransformer
        implements IClassTransformer, Opcodes {
    private static List<String> targets = new ArrayList<String>();

    static {
        targets.add("net.minecraft.client.multiplayer.PlayerControllerMP");
        targets.add("net.minecraft.client.renderer.EntityRenderer");
        targets.add("net.minecraft.network.NetHandlerPlayServer");
    }


    public byte[] transform(String name, String transformedName, byte[] bytes) {
        if (!targets.contains(name) && !targets.contains(transformedName)) {
            return bytes;
        }


        try {
            return hookMethod(bytes);

        } catch (Exception e) {

            e.printStackTrace();
            throw new RuntimeException("failed : ClayiumTransformer loading  name=" + name + " transformedName=" + transformedName + "", e);
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

    private static class MethodKey {
        String className;
        String methodName;
        String methodDesc;

        public MethodKey(String className, String methodName, String methodDesc) {
            this.className = className;
            this.methodName = methodName;
            this.methodDesc = methodDesc;
        }

        public int hashCode() {
            int prime = 31;
            int result = 1;
            result = 31 * result + ((this.className == null) ? 0 : this.className.hashCode());
            result = 31 * result + ((this.methodDesc == null) ? 0 : this.methodDesc.hashCode());
            result = 31 * result + ((this.methodName == null) ? 0 : this.methodName.hashCode());
            return result;
        }

        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            MethodKey other = (MethodKey) obj;
            if (this.className == null) {
                if (other.className != null)
                    return false;
            } else if (!this.className.equals(other.className)) {
                return false;
            }
            if (this.methodDesc == null) {
                if (other.methodDesc != null)
                    return false;
            } else if (!this.methodDesc.equals(other.methodDesc)) {
                return false;
            }
            if (this.methodName == null) {
                if (other.methodName != null)
                    return false;
            } else if (!this.methodName.equals(other.methodName)) {
                return false;
            }
            return true;
        }
    }

    private static List<MethodKey> methodList = new ArrayList<MethodKey>();
    private AbstractInsnNode[] test;

    private MethodNode getMethodNode(ClassNode classNode, String className, String methodName, String methodDesc) {
        if (classNode == null || classNode.name == null || !classNode.name.equals(className))
            return null;
        MethodKey key = new MethodKey(className, methodName, methodDesc);
        if (methodList.contains(key))
            return null;
        for (MethodNode curMnode : classNode.methods) {

            if (methodName.equals(curMnode.name) && methodDesc.equals(curMnode.desc)) {
                methodList.add(key);
                this.logger.info("Get MethodNode (" + className + ", " + methodName + ", " + methodDesc + ")");
                return curMnode;
            }
        }
        return null;
    }

    private AbstractInsnNode[] test2;

    private AbstractInsnNode convertInsnD2F(AbstractInsnNode insn) {
        if (insn instanceof LdcInsnNode && ((LdcInsnNode) insn).cst instanceof Double) {

            double d = ((Double) ((LdcInsnNode) insn).cst).doubleValue();
            if (d == 2.0D) {
                return (AbstractInsnNode) new InsnNode(13);
            }
            return (AbstractInsnNode) new LdcInsnNode(Float.valueOf((float) d));
        }
        if (insn.getOpcode() == 14)
            return (AbstractInsnNode) new InsnNode(11);
        if (insn.getOpcode() == 15) {
            return (AbstractInsnNode) new InsnNode(12);
        }
        return null;
    }


    private Logger logger = LogManager.getLogger("ClayiumTransformer");

    private byte[] hookMethod(byte[] bytes) {
        boolean flag = false;
        boolean deobf = false;

        ClassNode cnode = new ClassNode();
        ClassReader reader = new ClassReader(bytes);
        reader.accept((ClassVisitor) cnode, 0);


        MethodNode mnode = getMethodNode(cnode, "net/minecraft/client/multiplayer/PlayerControllerMP", "getBlockReachDistance", "()F");
        if (mnode == null) {
            mnode = getMethodNode(cnode, "net/minecraft/client/multiplayer/PlayerControllerMP", "func_78757_d", "()F");
            deobf = false;
        } else {
            deobf = true;
        }
        if (mnode != null) {

            flag = true;
            List<AbstractInsnNode> targetInsnList = new ArrayList<AbstractInsnNode>();
            for (AbstractInsnNode inst : mnode.instructions.toArray()) {


                if (inst.getNext() != null && inst.getNext().getOpcode() == 174) {
                    targetInsnList.add(inst);
                }
            }
            for (AbstractInsnNode targetInsn : targetInsnList) {
                mnode.instructions.insert(targetInsn, (AbstractInsnNode) new MethodInsnNode(184, "mods/clayium/item/gadget/GadgetLongArm", "hookBlockReachDistance", "(F)F", false));
            }
        }

        mnode = getMethodNode(cnode, "net/minecraft/client/renderer/EntityRenderer", "getMouseOver", "(F)V");
        if (mnode == null) {
            mnode = getMethodNode(cnode, "net/minecraft/client/renderer/EntityRenderer", "func_78473_a", "(F)V");
            deobf = false;
        } else {
            deobf = true;
        }
        if (mnode != null) {

            flag = true;
            List<AbstractInsnNode> targetInsnList = new ArrayList<AbstractInsnNode>();
            int d0var = -1;
            int d1var = -1;
            for (AbstractInsnNode insn : mnode.instructions.toArray()) {


                if (d0var == -1 && insn.getOpcode() == 182 && insn instanceof MethodInsnNode && ((MethodInsnNode) insn).desc.equals("()F") && ((MethodInsnNode) insn).name.equals(deobf ? "getBlockReachDistance" : "func_78757_d") && ((MethodInsnNode) insn).owner.equals("net/minecraft/client/multiplayer/PlayerControllerMP")) {
                    AbstractInsnNode abstractInsnNode = insn.getNext();
                    if (abstractInsnNode != null && abstractInsnNode.getOpcode() == 141) {
                        abstractInsnNode = abstractInsnNode.getNext();
                        if (abstractInsnNode != null && abstractInsnNode.getOpcode() == 57 && abstractInsnNode instanceof VarInsnNode) {
                            d0var = ((VarInsnNode) abstractInsnNode).var;
                        }
                    }
                }

                if (d1var == -1 && insn.getOpcode() == 24 && insn instanceof VarInsnNode && ((VarInsnNode) insn).var == d0var) {
                    AbstractInsnNode abstractInsnNode = insn.getNext();
                    if (abstractInsnNode != null && abstractInsnNode.getOpcode() == 57 && abstractInsnNode instanceof VarInsnNode) {
                        d1var = ((VarInsnNode) abstractInsnNode).var;
                    }
                }


                if (insn.getOpcode() == 182 && insn instanceof MethodInsnNode && ((MethodInsnNode) insn).desc.equals("(DDD)Lnet/minecraft/util/Vec3;") && ((MethodInsnNode) insn).name.equals(deobf ? "addVector" : "func_72441_c") && ((MethodInsnNode) insn).owner.equals("net/minecraft/util/Vec3")) {
                    d0var = -2;
                    d1var = -2;
                }

                AbstractInsnNode insn0 = insn.getNext();
                if (insn0 != null && insn0.getOpcode() == 57 && insn0 instanceof VarInsnNode && d0var == ((VarInsnNode) insn0).var &&
                        convertInsnD2F(insn) != null) {
                    targetInsnList.add(insn);
                }


                insn0 = insn.getNext();
                if (insn0 != null && insn0.getOpcode() == 57 && insn0 instanceof VarInsnNode && d1var == ((VarInsnNode) insn0).var &&
                        convertInsnD2F(insn) != null) {
                    targetInsnList.add(insn);
                }


                insn0 = insn.getPrevious();
                if (insn0 != null && insn0.getOpcode() == 24 && insn0 instanceof VarInsnNode && d0var == ((VarInsnNode) insn0).var) {
                    insn0 = insn.getNext();
                    if (insn0 != null && insn0.getOpcode() == 151 &&
                            convertInsnD2F(insn) != null) {
                        targetInsnList.add(insn);
                    }
                }
            }

            for (AbstractInsnNode targetInsn : targetInsnList) {

                AbstractInsnNode insn0 = convertInsnD2F(targetInsn);

                mnode.instructions.set(targetInsn, insn0);
                mnode.instructions.insert(insn0, (AbstractInsnNode) new MethodInsnNode(184, "mods/clayium/item/gadget/GadgetLongArm", "hookBlockReachDistance", "(F)F", false));

                mnode.instructions.insert(insn0.getNext(), (AbstractInsnNode) new InsnNode(141));
            }
            AbstractInsnNode[] array = mnode.instructions.toArray();
            this.logger.info("Transformed " + mnode.name);
        }
        mnode = getMethodNode(cnode, "net/minecraft/network/NetHandlerPlayServer", "processUseEntity", "(Lnet/minecraft/network/play/client/C02PacketUseEntity;)V");
        if (mnode == null) {
            mnode = getMethodNode(cnode, "net/minecraft/network/NetHandlerPlayServer", "func_147340_a", "(Lnet/minecraft/network/play/client/C02PacketUseEntity;)V");
            deobf = false;
        } else {
            deobf = true;
        }
        if (mnode != null) {

            flag = true;
            List<AbstractInsnNode> targetInsnList = new ArrayList<AbstractInsnNode>();
            int flagvar = -1;
            int d0var = -1;
            for (AbstractInsnNode insn : mnode.instructions.toArray()) {


                if (flagvar == -1 && d0var == -1 && insn.getOpcode() == 182 && insn instanceof MethodInsnNode && ((MethodInsnNode) insn).desc.equals("(Lnet/minecraft/entity/Entity;)Z") && ((MethodInsnNode) insn).name.equals(deobf ? "canEntityBeSeen" : "func_70685_l") && ((MethodInsnNode) insn).owner.equals("net/minecraft/entity/player/EntityPlayerMP")) {
                    AbstractInsnNode abstractInsnNode = insn.getNext();
                    if (abstractInsnNode != null && abstractInsnNode.getOpcode() == 54 && abstractInsnNode instanceof VarInsnNode) {
                        flagvar = ((VarInsnNode) abstractInsnNode).var;
                    }
                }

                AbstractInsnNode insn0 = insn.getNext();
                if (flagvar >= 0 && d0var == -1 && insn0 != null && insn0.getOpcode() == 57 && insn0 instanceof VarInsnNode) {
                    if (convertInsnD2F(insn) != null) {
                        d0var = ((VarInsnNode) insn0).var;
                        targetInsnList.add(insn);
                    }
                } else if (insn0 != null && insn0.getOpcode() == 57 && insn0 instanceof VarInsnNode && d0var == ((VarInsnNode) insn0).var &&
                        convertInsnD2F(insn) != null) {
                    targetInsnList.add(insn);
                }
            }

            for (AbstractInsnNode targetInsn : targetInsnList) {
                AbstractInsnNode insn0 = convertInsnD2F(targetInsn);
                mnode.instructions.set(targetInsn, insn0);
                mnode.instructions.insert(insn0, (AbstractInsnNode) new VarInsnNode(25, 0));
                insn0 = insn0.getNext();
                mnode.instructions.insert(insn0, (AbstractInsnNode) new FieldInsnNode(180, "net/minecraft/network/NetHandlerPlayServer", deobf ? "playerEntity" : "field_147369_b", "Lnet/minecraft/entity/player/EntityPlayerMP;"));
                insn0 = insn0.getNext();
                mnode.instructions.insert(insn0, (AbstractInsnNode) new MethodInsnNode(184, "mods/clayium/item/gadget/GadgetLongArm", "hookBlockReachDistanceSq", "(FLnet/minecraft/entity/player/EntityPlayer;)F", false));
                insn0 = insn0.getNext();
                mnode.instructions.insert(insn0, (AbstractInsnNode) new InsnNode(141));
            }
            AbstractInsnNode[] array = mnode.instructions.toArray();
            this.logger.info("Transformed " + mnode.name);
        }


        if (flag) {

            ClassWriter cw = new ClassWriter(3);
            cnode.accept(cw);
            bytes = cw.toByteArray();
        }
        return bytes;
    }
}
