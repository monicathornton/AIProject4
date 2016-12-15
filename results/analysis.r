setwd("C:/Users/Jani/Documents/ArtificialIntelligence/AIProject4/results/") #Set working directory
###################
#Read in test data
qll = read.csv("qll.csv",sep=",",header=F)
qlo = read.csv("qlo.csv",sep=",",header=F)
qlr = read.csv("qlrB.csv",sep=",",header=F)
qlrW = read.csv("qlrW.csv",sep=",",header=F)

vil = read.csv("vil.csv",sep=",",header=F)
vio = read.csv("vio.csv",sep=",",header=F)
vir = read.csv("vir.csv",sep=",",header=F)
virW = read.csv("virW.csv",sep=",",header=F)

##########################
# add column names
colnames(qll) <- c("Moves", "Crashes")
colnames(qlo) <- c("Moves", "Crashes")
colnames(qlr) <- c("Moves", "Crashes")
colnames(qlrW) <- c("Moves", "Crashes")

colnames(vil) <- c("Moves", "Crashes")
colnames(vio) <- c("Moves", "Crashes")
colnames(vir) <- c("Moves", "Crashes")
colnames(virW) <- c("Moves", "Crashes")


#########################
# Test imputation

t.test(qll$Moves, vil$Moves, var.equal = FALSE)
t.test(qll$Crashes, vil$Crashes, var.equal = FALSE)

t.test(qlo$Moves, vio$Moves, var.equal = FALSE)
t.test(qlo$Crashes, vio$Crashes, var.equal = FALSE)

t.test(qlr$Moves, vir$Moves, var.equal = FALSE)
t.test(qlr$Crashes, vir$Crashes, var.equal = FALSE)

t.test(qlrW$Moves, virW$Moves, var.equal = FALSE)
t.test(qlrW$Crashes, virW$Crashes, var.equal = FALSE)
