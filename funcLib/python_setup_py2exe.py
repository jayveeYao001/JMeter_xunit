#encoding=utf-8

from distutils.core import setup
import py2exe

options = {"py2exe":{"compressed": 1, #压缩   
	        "optimize": 2,  
	        "bundle_files": 1 # 所有文件打包成一个exe文件 【win64系统中只能是3】 
	}}

setup(console=["ATLayerJtl.py"], options=options, zipfile=None)