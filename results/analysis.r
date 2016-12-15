setwd("C:/Users/Jani/Documents/ArtificialIntelligence/AIProject4/results/") #Set working directory
###################
#Read in test data
qll = read.csv("qll.csv",sep=",",header=F)
qlo = read.csv("qlo.csv",sep=",",header=F)
qlr = read.csv("qlrB.csv",sep=",",header=F)
qlrW = read.csv("qlrW.csv",sep=",",header=F)

vil = read.csv("vill.csv",sep=",",header=F)
vio = read.csv("vio.csv",sep=",",header=F)
vir = read.csv("vir.csv",sep=",",header=F)
virW = read.csv("virW.csv",sep=",",header=F)

##########################
# add column names
colnames(qll) <- c("Iteration", "Moves", "Crashes")
colnames(qlo) <- c("Iteration", "Moves", "Crashes")
colnames(qlr) <- c("Iteration", "Moves", "Crashes")
colnames(qlrW) <- c("Iteration", "Moves", "Crashes")

colnames(vil) <- c("Iteration", "Moves", "Crashes")
colnames(vio) <- c("Iteration", "Moves", "Crashes")
colnames(vir) <- c("Iteration", "Moves", "Crashes")
colnames(virW) <- c("Iteration", "Moves", "Crashes")


#########################
# Test imputation

t.test(houseknnT$Accuracy, houseknnF$Accuracy, var.equal = FALSE)
