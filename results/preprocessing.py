# -*- coding: utf-8 -*-
"""
Created on Wed Dec 14 09:20:12 2016

@author: Jani
"""
import os
import csv


def removeHeader(fp, fpstr, tfpstr):
    #print(fp)
    print(fpstr)
    print(tfpstr)
    with open(fpstr, "w", newline='') as curvefile:
        curvewriter = csv.writer(curvefile, delimiter=",", quotechar="|")
        with open(fp, 'r') as data:
            datareader = csv.reader(data, delimiter=',', quotechar='|')
            next(datareader)
            next(datareader)
            for row in datareader:
                #print(row)
                if row[3] == 'testing':
                    createTestFile(tfpstr, row)
                    #write to testfile
                else:
                    #write to curvefile
                    curvewriter.writerow(row[4:])
        curvefile.close()
                    


def createTestFile(fpstr, arr):
    with open(fpstr, "a") as test_file:
        testwriter = csv.writer(test_file, delimiter=",", quotechar="|")
        testwriter.writerow(arr[5:])
        test_file.close
    
def main():
    partpath = "C:\\Users\\Jani\\Documents\\ArtificialIntelligence\\AIProject4\\experimentruns"
    for file in os.listdir(partpath):
        if file.endswith(".txt"):
            if file.startswith("q"):
                pathfile = os.path.join(partpath, file)
                f = file.split(".")
                ftp = f[0]
                if ftp[-1:] == "0":
                    ftp1 = ftp[:-2]
                else:
                    ftp1 = ftp[:-1]
                ftp1 = ftp1+".csv"
                ftp2 = ftp+"c.csv"
                #print(ftp1)
                removeHeader(pathfile, ftp2, ftp1)
            
            #print(file)
            #runExperiments(pathfile)

if __name__ == "__main__": main()