package code.mission;

public class Grid {
    int c;
    int k;
    int n;
    int m;
    int ex, ey;
    int sx, sy;
    int[] x;
    int[] y;
    int[] h;

    public Grid(int c, int k, int n, int m, int ex, int ey, int sx, int sy, int[] x, int[] y, int[] h) {
        this.c = c;
        this.k = k;
        this.n = n;
        this.m = m;
        this.ex = ex;
        this.ey = ey;
        this.sx = sx;
        this.sy = sy;
        this.x = x;
        this.y = y;
        this.h = h;
    }

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(n + "," + m + ";").append(ex + "," + ey + ";").append(sx + "," + sy + ";");
		for (int i = 0; i < x.length - 1; ++i)
			sb.append(x[i] + "," + y[i] + ",");
		if (x.length > 0)
			sb.append(x[x.length - 1] + "," + y[x.length - 1] + ";");
		for (int i = 0; i < h.length - 1; ++i)
			sb.append(h[i] + ",");
		if (h.length > 0)
			sb.append(h[h.length - 1] + ";");
		sb.append(c);
		return sb.toString();
	}

	public static Grid unparse(String grid) {
		String[] params = grid.split(";");
		int[] a = splitUnparse(params[0]);
		int n = a[0], m = a[1];
		int[] e = splitUnparse(params[1]);
		int ex = e[0], ey = e[1];
		int[] s = splitUnparse(params[2]);
		int sx = s[0], sy = s[1];
		int[] pos = splitUnparse(params[3]);
		int[] h = splitUnparse(params[4]);
		int k = h.length;
		int[] x = new int[k], y = new int[k];
		for (int i = 0, j = 0; j<k; i += 2, j++) {
			x[j] = pos[i];
			y[j] = pos[i + 1];
		}
		int[] c = splitUnparse(params[5]);
//		System.err.println(c[0]);
//		System.err.println(sx+" "+sy+" ");
		return new Grid(c[0], k, n, m, ex, ey, sx, sy, x, y, h);
	}

    public static int[] splitUnparse(String s) {
        String[] tmp = s.split(",");
        int[] ans = new int[tmp.length];
        for (int i = 0; i < ans.length; i++)
            ans[i] = Integer.parseInt(tmp[i]);
        return ans;
    }

    public int getOriginalHealth(int i) {
        return h[i];
    }
}
