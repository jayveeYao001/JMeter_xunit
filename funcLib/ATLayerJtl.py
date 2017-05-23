# encoding=utf-8

import xml.etree.ElementTree as ET
import sys

if __name__ == "__main__":
    def if_main():
        pass
    print 'proj=%r[1]' % sys.argv
    proj = sys.argv[1]    
    
    # 1) 读取xml文件
    tree = ET.parse("../_tempFiles/ATReport_%s.jtl" % proj)
    root = tree.getroot()

    # 2) 按TestModule进行一级分层
    tmSeqs = []   # exam: [1, 79]
    for i in xrange(len(root)):
        if root[i].get('lb', '').startswith("@TM"):
            tmSeqs.append(i)
    tmSeqFull = tmSeqs + [len(root)]      # exam: [1, 79, 92]
    tmNeedRmvChilds = []
    for i in xrange(len(tmSeqs)):
        tmSeq = tmSeqs[i]
        tmEle = root[tmSeq]
        for j in xrange(tmSeq+1, tmSeqFull[i+1]):  # exam: 2<=j<=78
            tmEle.append(root[j])
            tmNeedRmvChilds.append(root[j])
    for tmNeedRmvChild in tmNeedRmvChilds:
        root.remove(tmNeedRmvChild)

    # 3) 按TestItem进行二级分层
    for tm in root:
        tiSeqs = []     # exam: [9, 17, 19, 31, 33, 35, 37, 39, 41, 43, 45, 47, 49, 51, 53, 55]
        for i in xrange(len(tm)):
            if tm[i].get('lb', '').startswith("@TI"):
                tiSeqs.append(i)
        tiSeqFull = tiSeqs + [len(tm)]   # exam: [9, 17, 19, 31, 33, 35, 37, 39, 41, 43, 45, 47, 49, 51, 53, 55, 82]

        tiNeedRmvChilds = []
        for i in xrange(len(tiSeqs)):
            tiSeq = tiSeqs[i]
            tiEle = tm[tiSeq]
            for j in xrange(tiSeq+1, tiSeqFull[i+1]):  # exam: 10<=j<=16
                tiEle.append(tm[j])
                tiNeedRmvChilds.append(tm[j])
        for tiNeedRmvChild in tiNeedRmvChilds:
            tm.remove(tiNeedRmvChild)

    # 4) 按TestItem，清除len(testSon<@lb!=''>)==1的
    for tm in root:
        tmNeedRmvChilds = []
        for i in xrange(len(tm)):
            ti = tm[i]
            if ti.get('lb', '') == '':
                continue
            if len([i for i in ti if i.get('lb', '') != '']) == 1:
                # print ET.dump(ti)
                tmNeedRmvChilds.append(ti)
        for tmNeedRmvChild in tmNeedRmvChilds:
            tm.remove(tmNeedRmvChild)

    # 5) 按TestItem进行二级测试结果收集
    for tm in root:
        tm.set("ATRes", "true")
        for ti in tm:
            ti.set("ATRes", "true")
            for testSon in ti:
                if testSon.get("s", "") == "false":
                    ti.set("ATRes", "false")
                    break
            if ti.get("ATRes") == "false":
                tm.set("ATRes", "false")

    # 6) 写入新的xml文件  # print ET.dump(ti)
    tree.write('../_tempFiles/ATReport_%s_new.jtl' % proj)






