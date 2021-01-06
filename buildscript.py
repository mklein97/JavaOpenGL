
import threading
import hashlib
import urllib.request 
import os
import zipfile 
import subprocess
import os.path 
import queue 
import shutil
import sys

#download and build the Windows version of assimp
#Need to have VS 2017 installed...

cmake_url = "https://cmake.org/files/v3.9/cmake-3.9.1-win64-x64.zip"
cmake_hash = "a3226743c0b160f8f402aa72103a2ec391a8ba5556c49ddd047b1c62a9f75ffb"
assimp_url = "https://github.com/assimp/assimp/archive/v3.3.1.zip"
assimp_hash = None

Q = queue.Queue()
def stream_reader(stream):
    while 1:
        line = stream.readline()
        if not line:
            break
        Q.put(line)

def queuereader():
    while 1:
        x = Q.get()
        print(x.decode(errors="ignore"))
    


def main():
    cmake_zip = get_if_needed(cmake_url,cmake_hash)
    assimp_zip = get_if_needed(assimp_url,assimp_hash)

    expand(cmake_zip)
    expand(assimp_zip)

    pwd = os.path.abspath(os.getcwd())
    
    print("1. Start the Visual Studio x64 Native command prompt")
    print("2. Then copy/paste this:")
    cmake = os.path.abspath(os.path.join(pwd,"cmake-3.9.1-win64-x64","bin","cmake-gui"))
    print('"'+cmake+'"')
    print("3. Use this as the Source directory: ")
    print(os.path.abspath(os.path.join(pwd,"assimp-3.3.1")))
    print("4. Create a new folder and choose that as the Build directory")
    print("5. Choose 'Configure' and make sure to choose the VS 2017 Win64 platform")
    print("   If you get an error about being unable to find the DX libraries,")
    print("   type 'view' in the 'search' box and uncheck the 'Build viewer' option.")
    print("   Then click configure again.")
    print("6. Choose 'Generate'")
    print("7. In File Explorer, navigate to the Build directory")
    print("8. Double click the 'assimp.sln' file")
    print("9. Right click 'assimp' and choose 'Build'")
    print("10. Look for the assimp-vc140-mt.dll in your build directory.")
    input("Press 'enter' to quit...")
    sys.exit(0)
        
    
    
def expand(fname):
    print("Decompressing",fname,"...")
    zfp = zipfile.ZipFile(fname)
    infolist = zfp.infolist()
    for i in range(len(infolist)):
        print(i+1,"of",len(infolist))
        zfp.extract(infolist[i])
        
def get_if_needed(url,hash_):
    
    filename = url.split("/")[-1]

    if os.access(filename,os.F_OK):
        print("Examining",filename,"...")
        if hash_ == None:
            #we don't have a hash, so accept it
            return filename
            
        H = hashlib.sha256()
        with open(filename,"rb") as fp:
            while 1:
                dat = fp.read(4096)
                if len(dat) == 0:
                    break
                H.update(dat)
        hh = H.hexdigest()
        
        if hh == hash_ :
            print(filename,"is OK")
            return filename

    print("Downloading",filename,"from",url)
    with urllib.request.urlopen(url) as req:
        with open(filename,"wb") as fp:
            H = hashlib.sha256()
            meta = req.info()
            totalsize = meta["Content-length"]
            if totalsize == None:
                totalsize="file"
                
            nextprint=0
            while 1:
                dat = req.read(4096)
                if len(dat) == 0:
                    break
                fp.write(dat)
                H.update(dat)
                if fp.tell() >= nextprint:
                    print(filename,":",fp.tell(),"of",totalsize)
                    nextprint += 100*1024
            hh = H.hexdigest()
            if hash_ != None and hh != hash_:
                print("Hash mismatch for",filename)
                sys.exit(1)
    return filename
    
    
            
main()
    
