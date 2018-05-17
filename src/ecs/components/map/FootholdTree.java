package ecs.components.map;

import com.artemis.Component;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class FootholdTree extends Component {

    /**
     *
     * @author Matze
     */
    private FootholdTree nw = null;
    private FootholdTree ne = null;
    private FootholdTree sw = null;
    private FootholdTree se = null;
    private List<Foothold> footholds = new LinkedList<Foothold>();
    public Point lBound;
    public Point uBound;
    private Point center;
    private int depth = 0;
    private static int maxDepth = 8;
    private int maxDropX;
    private int minDropX;

    public FootholdTree() {
    }

    public FootholdTree(Point p1, Point uBound, int depth) {
        this.lBound = p1;
        this.uBound = uBound;
        this.depth = depth;
        center = new Point((uBound.x - p1.x) / 2, (uBound.y - p1.y) / 2);
    }

    public List<Foothold> getFootholds() {
        return footholds;
    }

    public void insert(Foothold f) {
        if (depth == 0) {
            if (f.getX1() > maxDropX) {
                maxDropX = f.getX1();
            }
            if (f.getX1() < minDropX) {
                minDropX = f.getX1();
            }
            if (f.getX2() > maxDropX) {
                maxDropX = f.getX2();
            }
            if (f.getX2() < minDropX) {
                minDropX = f.getX2();
            }
        }
        if (depth == maxDepth ||
                (f.getX1() >= lBound.x && f.getX2() <= uBound.x &&
                        f.getY1() >= lBound.y && f.getY2() <= uBound.y)) {
            footholds.add(f);
        } else {
            if (nw == null) {
                nw = new FootholdTree(lBound, center, depth + 1);
                ne = new FootholdTree(new Point(center.x, lBound.y), new Point(uBound.x, center.y), depth + 1);
                sw = new FootholdTree(new Point(lBound.x, center.y), new Point(center.x, uBound.y), depth + 1);
                se = new FootholdTree(center, uBound, depth + 1);
            }
            if (f.getX2() <= center.x && f.getY2() <= center.y) {
                nw.insert(f);
            } else if (f.getX1() > center.x && f.getY2() <= center.y) {
                ne.insert(f);
            } else if (f.getX2() <= center.x && f.getY1() > center.y) {
                sw.insert(f);
            } else {
                se.insert(f);
            }
        }
    }

    private List<Foothold> getRelevants(Point p) {
        return getRelevants(p, new LinkedList<Foothold>());
    }

    private List<Foothold> getRelevants(Point p, List<Foothold> list) {
        list.addAll(footholds);
        if (nw != null) {
            if (p.x <= center.x && p.y <= center.y) {
                nw.getRelevants(p, list);
            } else if (p.x > center.x && p.y <= center.y) {
                ne.getRelevants(p, list);
            } else if (p.x <= center.x && p.y > center.y) {
                sw.getRelevants(p, list);
            } else {
                se.getRelevants(p, list);
            }
        }
        return list;
    }

    private Foothold findWallR(Point p1, Point p2) {
        Foothold ret;
        for (Foothold f : footholds) {
            if (f.isWall() && f.getX1() >= p1.x && f.getX1() <= p2.x &&
                    f.getY1() >= p1.y && f.getY2() <= p1.y) {
                return f;
            }
        }
        if (nw != null) {
            if (p1.x <= center.x && p1.y <= center.y) {
                ret = nw.findWallR(p1, p2);
                if (ret != null) {
                    return ret;
                }
            }
            if ((p1.x > center.x || p2.x > center.x) && p1.y <= center.y) {
                ret = ne.findWallR(p1, p2);
                if (ret != null) {
                    return ret;
                }
            }
            if (p1.x <= center.x && p1.y > center.y) {
                ret = sw.findWallR(p1, p2);
                if (ret != null) {
                    return ret;
                }
            }
            if ((p1.x > center.x || p2.x > center.x) && p1.y > center.y) {
                ret = se.findWallR(p1, p2);
                if (ret != null) {
                    return ret;
                }
            }
        }
        return null;
    }

    public Foothold findWall(Point p1, Point p2) {
        if (p1.y != p2.y) {
            throw new IllegalArgumentException();
        }
        return findWallR(p1, p2);
    }


    /**

     int __cdecl is_blocked_area(CStaticFoothold *pfh1, CStaticFoothold *pfh2, int x, int y)
     {
     int v4; // eax@1
     int v5; // edi@1
     int v6; // ecx@1
     int v8; // [sp+Ch] [bp-8h]@1
     int v9; // [sp+10h] [bp-4h]@1
     CStaticFoothold *pfh1a; // [sp+1Ch] [bp+8h]@1

     v4 = pfh2->m_x2;
     v9 = pfh1->m_y2;
     v5 = pfh1->m_x1;
     v6 = pfh2->m_y2;
     v8 = pfh1->m_x2;
     pfh1a = (CStaticFoothold *)pfh1->m_y1;
     if ( (v8 - v5) * (v6 - (signed int)pfh1a) - (v4 - v5) * (v9 - (signed int)pfh1a) <= 0 )
     {
     if ( (y - (signed int)pfh1a) * (v8 - v5) - (x - v5) * (v9 - (signed int)pfh1a) > 0 )
     return 1;
     }
     else if ( (y - (signed int)pfh1a) * (v8 - v5) - (x - v5) * (v9 - (signed int)pfh1a) <= 0 )
     {
     return 0;
     }
     if ( (y - pfh2->m_y1) * (v4 - pfh2->m_x1) - (x - pfh2->m_x1) * (v6 - pfh2->m_y1) <= 0 )
     return 0;
     return 1;
     }








     void __thiscall CStaticFoothold::ValidateVectorInfo(CStaticFoothold *this)
     {
     CStaticFoothold *v1; // esi@1
     double v2; // ST0C_8@1
     long double dy; // ST14_8@1
     double v4; // st7@1

     v1 = this;
     v2 = (double)(this->m_x2 - this->m_x1);
     dy = (double)(this->m_y2 - this->m_y1);
     v4 = sqrt(dy * dy + v2 * v2);
     v1->m_len = v4;
     v1->m_uvx = v2 / v4;
     v1->m_uvy = dy / v4;
     }



     void __thiscall CAttrFoothold::CAttrFoothold(CAttrFoothold *this)
     {
     CAttrFoothold *v1; // esi@1

     v1 = this;
     this->vfptr = (ZRefCountedVtbl *)&ZRefCounted::`vftable';
     memset(this, 0, 0xCu);
     v1->walk = 1.0;
     v1->vfptr = (ZRefCountedVtbl *)&CAttrFoothold::`vftable';
     v1->drag = 1.0;
     v1->force = 0.0;
     }



     void __thiscall CStaticFoothold::CStaticFoothold(CStaticFoothold *this)
     {
     CStaticFoothold *v1; // esi@1

     v1 = this;
     this->vfptr = (ZRefCountedVtbl *)&ZRefCounted::`vftable';
     memset(this, 0, 0xCu);
     v1->m_pAttrFoothold.p = 0;
     v1->m_dwSN = 0;
     v1->vfptr = (ZRefCountedVtbl *)&CStaticFoothold::`vftable';
     }


     345 CStaticFoothold struct __cppobj __declspec(align(8)) : ZRefCounted {
     int m_x1;//0
     int m_y1;//4
     int m_x2;//8
     int m_y2;//12
     int m_lPage;//16
     int m_lZMass;//20
     ZRef<CAttrFoothold> m_pAttrFoothold;
     long double m_uvx;
     long double m_uvy;
     long double m_len;
     unsigned int m_dwSN;
     ===========================================================================================================
     if ( lRes._m_pHead )
     {
     do
     {
     v6 = (CStaticFoothold *)-(v5 != 0);
     v7 = (CStaticFoothold *)((char *)v5 - 16);
     v8 = (CStaticFoothold *)v5->_m_nRef;
     m_x2 = v8->m_x2;
     v13 = (CStaticFoothold *)(*(_DWORD *)(((unsigned int)v7 & (unsigned int)v6) + 4) != 0 ? *(_DWORD *)(((unsigned int)v7 & (unsigned int)v6) + 4)
     + 16 : 0);
     m_x1 = v8->m_x1;
     if ( m_x1 < m_x2 && m_x1 <= x && m_x2 >= x )
     {
     v11 = v8->m_y1 + (x - m_x1) * (v8->m_y2 - v8->m_y1) / (m_x2 - m_x1);
     if ( v11 >= y && v11 < v16 )
     {
     v16 = v8->m_y1 + (x - m_x1) * (v8->m_y2 - v8->m_y1) / (m_x2 - m_x1);
     *pcy = v11;
     v14 = v8;
     }
     }
     v5 = v13;
     }
     while ( v13 );
     }

     */

    public Foothold findBelow(Point p) {
        List<Foothold> relevants = getRelevants(p);
        Foothold match = null;
        int yBound = Integer.MAX_VALUE;
        for (Foothold fh : relevants) {
            if (fh.getX1() < fh.getX2() && fh.getX1() <= p.x && fh.getX2() >= p.x) {
                int slope = fh.getY1() + (p.x - fh.getX1()) * (fh.getY2() - fh.getY1()) / (fh.getX2() - fh.getX1());
                if (slope >= p.y && slope < yBound) {
                    yBound = fh.getY1() + (p.x - fh.getX1()) * (fh.getY2() - fh.getY1()) / (fh.getX2() - fh.getX1());
                    match = fh;
                }
            }

        }
        //System.out.println(match.getId());
        return match;
    }

/*    public Foothold findBelow(Point p) {
    List<Foothold> relevants = getRelevants(p);
    List<Foothold> xMatches = new LinkedList<Foothold>();
    for (Foothold fh : relevants) {
        if (fh.getX1() <= p.x && fh.getX2() >= p.x) {
            xMatches.add(fh);
        }
    }
    Collections.sort(xMatches);
    for (Foothold fh : xMatches) {
        if (!fh.isWall() && fh.getY1() != fh.getY2()) {
            int calcY;
            double s1 = Math.abs(fh.getY2() - fh.getY1());
            double s2 = Math.abs(fh.getX2() - fh.getX1());
            double s4 = Math.abs(p.x - fh.getX1());
            double alpha = Math.atan(s2 / s1);
            double beta = Math.atan(s1 / s2);
            double s5 = Math.cos(alpha) * (s4 / Math.cos(beta));
            if (fh.getY2() < fh.getY1()) {
                calcY = fh.getY1() - (int) s5;
            } else {
                calcY = fh.getY1() + (int) s5;
            }
            if (calcY >= p.y) {
                return fh;
            }
        } else if (!fh.isWall()) {
            if (fh.getY1() >= p.y) {
                return fh;
            }
        }
    }
    return null;
} */

    public int getX1() {
        return lBound.x;
    }

    public int getX2() {
        return uBound.x;
    }

    public int getY1() {
        return lBound.y;
    }

    public int getY2() {
        return uBound.y;
    }

    public int getMaxDropX() {
        return maxDropX;
    }

    public int getMinDropX() {
        return minDropX;
    }

    public static class Foothold {
        private Point p1;
        private Point p2;
        private int id;
        private int next, prev;

        public Foothold(Point p1, Point p2, int id) {
            this.p1 = p1;
            this.p2 = p2;
            this.id = id;
        }

        public boolean isWall() {
            return p1.x == p2.x;
        }

        public int getX1() {
            return p1.x;
        }

        public int getX2() {
            return p2.x;
        }

        public int getY1() {
            return p1.y;
        }

        public int getY2() {
            return p2.y;
        }

        // may not be entirely accurate to the exact FH!
        public int calculateFooting(int x) {
            if (p1.y == p2.y) {
                return p2.y; // y at both ends is the same
            }
            double slope = (p1.y - p2.y) / (double) (p1.x - p2.x);

            return (int) ((slope * x) + p1.y - (slope * p1.x));
        }

        public int compareTo(Foothold o) {
            Foothold other = o;
            if (p2.y < other.getY1()) {
                return -1;
            } else if (p1.y > other.getY2()) {
                return 1;
            } else {
                return 0;
            }
        }

        public int getId() {
            return id;
        }

        public int getNext() {
            return next;
        }

        public void setNext(int next) {
            this.next = next;
        }

        public int getPrev() {
            return prev;
        }

        public void setPrev(int prev) {
            this.prev = prev;
        }
    }

}
