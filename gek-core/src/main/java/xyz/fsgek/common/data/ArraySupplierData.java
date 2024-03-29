package xyz.fsgek.common.data;

import xyz.fsgek.annotations.Nullable;
import xyz.fsgek.common.io.GekIO;
import xyz.fsgek.common.base.GekCheck;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.function.Supplier;

final class ArraySupplierData implements GekData {

    private final Supplier<byte[]> supplier;

    ArraySupplierData(Supplier<byte[]> supplier) {
        this.supplier = supplier;
    }

    @Override
    public synchronized byte[] toBytes() {
        return supplier.get();
    }

    @Override
    public synchronized int write(byte[] dest, int offset, int length) {
        GekCheck.checkRangeInBounds(offset, offset + length, 0, dest.length);
        byte[] bytes = supplier.get();
        int len = Math.min(bytes.length, length);
        System.arraycopy(bytes, 0, dest, offset, len);
        return len;
    }

    @Override
    public synchronized int write(ByteBuffer dest) {
        byte[] bytes = supplier.get();
        int len = Math.min(bytes.length, dest.remaining());
        if (len == 0) {
            return 0;
        }
        dest.put(bytes, 0, len);
        return len;
    }

    @Override
    public synchronized InputStream toInputStream() {
        return GekIO.toInputStream(supplier.get());
    }

    @Override
    public boolean hasBackArray() {
        return false;
    }

    @Override
    public @Nullable byte[] backArray() {
        return null;
    }
}
