setwd("C:/Users/Jani/Documents/ArtificialIntelligence/AIProject3/results/") #Set working directory
###################
#Read in test data
houseknnT = read.csv("houseknnimputationTrueTest.csv",sep=",",header=F)
houseknnF = read.csv("houseknnimputationFalseTest.csv",sep=",",header=F)
housenbT = read.csv("housenbimputationTrueTest.csv",sep=",",header=F)
housenbF = read.csv("housenbimputationFalseTest.csv",sep=",",header=F)
housetanT = read.csv("housetanimputationTrueTest.csv",sep=",",header=F)
housetanF = read.csv("housetanimputationFalseTest.csv",sep=",",header=F)
houseid3T = read.csv("houseid3imputationTrueTest.csv",sep=",",header=F)
houseid3F = read.csv("houseid3imputationFalseTest.csv",sep=",",header=F)

breastknnT = read.csv("breastknnimputationTrueTest.csv",sep=",",header=F)
breastknnF = read.csv("breastknnimputationFalseTest.csv",sep=",",header=F)
breastnbT = read.csv("breastnbimputationTrueTest.csv",sep=",",header=F)
breastnbF = read.csv("breastnbimputationFalseTest.csv",sep=",",header=F)
breasttanT = read.csv("breasttanimputationTrueTest.csv",sep=",",header=F)
breasttanF = read.csv("breasttanimputationFalseTest.csv",sep=",",header=F)
breastid3T = read.csv("breastid3imputationTrueTest.csv",sep=",",header=F)
breastid3F = read.csv("breastid3imputationFalseTest.csv",sep=",",header=F)

irisknn = read.csv("irisknnTest.csv",sep=",",header=F)
irisnb = read.csv("irisnbTest.csv",sep=",",header=F)
iristan = read.csv("iristanTest.csv",sep=",",header=F)
irisid3 = read.csv("irisid3Test.csv",sep=",",header=F)

glassknn = read.csv("glassknnTest.csv",sep=",",header=F)
glassnb = read.csv("glassnbTest.csv",sep=",",header=F)
glasstan = read.csv("glasstanTest.csv",sep=",",header=F)
glassid3 = read.csv("glassid3Test.csv",sep=",",header=F)

soyknn = read.csv("soybeanknnTest.csv",sep=",",header=F)
soynb = read.csv("soybeannbTest.csv",sep=",",header=F)
soytan = read.csv("soybeantanTest.csv",sep=",",header=F)
soyid3 = read.csv("soybeanid3Test.csv",sep=",",header=F)


######################
#Read in training error data

housenbTtrain = read.csv("housenbimputationTrueTrain.csv",sep=",",header=F)
housenbFtrain = read.csv("housenbimputationFalseTrain.csv",sep=",",header=F)
housetanTtrain = read.csv("housetanimputationTrueTrain.csv",sep=",",header=F)
housetanFtrain = read.csv("housetanimputationFalseTrain.csv",sep=",",header=F)
houseid3Ttrain = read.csv("houseid3imputationTrueTrain.csv",sep=",",header=F)
houseid3Ftrain = read.csv("houseid3imputationFalseTrain.csv",sep=",",header=F)

breastnbTtrain = read.csv("breastnbimputationTrueTrain.csv",sep=",",header=F)
breastnbFtrain = read.csv("breastnbimputationFalseTrain.csv",sep=",",header=F)
breasttanTtrain = read.csv("breasttanimputationTrueTrain.csv",sep=",",header=F)
breasttanFtrain = read.csv("breasttanimputationFalseTrain.csv",sep=",",header=F)
breastid3Ttrain = read.csv("breastid3imputationTrueTrain.csv",sep=",",header=F)
breastid3Ftrain = read.csv("breastid3imputationFalseTrain.csv",sep=",",header=F)

irisnbtrain = read.csv("irisnbTrain.csv",sep=",",header=F)
iristantrain = read.csv("iristanTrain.csv",sep=",",header=F)
irisid3train = read.csv("irisid3Train.csv",sep=",",header=F)

glassnbtrain = read.csv("glassnbTrain.csv",sep=",",header=F)
glasstantrain = read.csv("glasstanTrain.csv",sep=",",header=F)
glassid3train = read.csv("glassid3Train.csv",sep=",",header=F)

soynbtrain = read.csv("soybeannbTrain.csv",sep=",",header=F)
soytantrain = read.csv("soybeantanTrain.csv",sep=",",header=F)
soyid3train = read.csv("soybeanid3Train.csv",sep=",",header=F)

##########################
# add column names
colnames(houseknnT) <- c("RunName", "Accuracy", "Precision", "Recall", "Fscore")
colnames(houseknnF) <- c("RunName", "Accuracy", "Precision", "Recall", "Fscore")
colnames(housenbT) <- c("RunName", "Accuracy", "Precision", "Recall", "Fscore")
colnames(housenbF) <- c("RunName", "Accuracy", "Precision", "Recall", "Fscore")
colnames(houseid3T) <- c("RunName", "Accuracy", "Precision", "Recall", "Fscore")
colnames(houseid3F) <- c("RunName", "Accuracy", "Precision", "Recall", "Fscore")
colnames(housetanT) <- c("RunName", "Accuracy", "Precision", "Recall", "Fscore")
colnames(housetanF) <- c("RunName", "Accuracy", "Precision", "Recall", "Fscore")

colnames(housenbTtrain) <- c("RunName", "Accuracy", "Precision", "Recall", "Fscore")
colnames(housenbFtrain) <- c("RunName", "Accuracy", "Precision", "Recall", "Fscore")
colnames(houseid3Ttrain) <- c("RunName", "Accuracy", "Precision", "Recall", "Fscore")
colnames(houseid3Ftrain) <- c("RunName", "Accuracy", "Precision", "Recall", "Fscore")
colnames(housetanTtrain) <- c("RunName", "Accuracy", "Precision", "Recall", "Fscore")
colnames(housetanFtrain) <- c("RunName", "Accuracy", "Precision", "Recall", "Fscore")

colnames(breastknnT) <- c("RunName", "Accuracy", "Precision", "Recall", "Fscore")
colnames(breastknnF) <- c("RunName", "Accuracy", "Precision", "Recall", "Fscore")
colnames(breastnbT) <- c("RunName", "Accuracy", "Precision", "Recall", "Fscore")
colnames(breastnbF) <- c("RunName", "Accuracy", "Precision", "Recall", "Fscore")
colnames(breastid3T) <- c("RunName", "Accuracy", "Precision", "Recall", "Fscore")
colnames(breastid3F) <- c("RunName", "Accuracy", "Precision", "Recall", "Fscore")
colnames(breasttanT) <- c("RunName", "Accuracy", "Precision", "Recall", "Fscore")
colnames(breasttanF) <- c("RunName", "Accuracy", "Precision", "Recall", "Fscore")

colnames(breastnbTtrain) <- c("RunName", "Accuracy", "Precision", "Recall", "Fscore")
colnames(breastnbFtrain) <- c("RunName", "Accuracy", "Precision", "Recall", "Fscore")
colnames(breastid3Ttrain) <- c("RunName", "Accuracy", "Precision", "Recall", "Fscore")
colnames(breastid3Ftrain) <- c("RunName", "Accuracy", "Precision", "Recall", "Fscore")
colnames(breasttanTtrain) <- c("RunName", "Accuracy", "Precision", "Recall", "Fscore")
colnames(breasttanFtrain) <- c("RunName", "Accuracy", "Precision", "Recall", "Fscore")

colnames(glassnb) <- c("RunName", "Accuracy", "Precision", "Recall", "Fscore")
colnames(glassid3) <- c("RunName", "Accuracy", "Precision", "Recall", "Fscore")
colnames(glassknn) <- c("RunName", "Accuracy", "Precision", "Recall", "Fscore")
colnames(glasstan) <- c("RunName", "Accuracy", "Precision", "Recall", "Fscore")

colnames(glassnbtrain) <- c("RunName", "Accuracy", "Precision", "Recall", "Fscore")
colnames(glassid3train) <- c("RunName", "Accuracy", "Precision", "Recall", "Fscore")
colnames(glasstantrain) <- c("RunName", "Accuracy", "Precision", "Recall", "Fscore")

colnames(irisnb) <- c("RunName", "Accuracy", "Precision", "Recall", "Fscore")
colnames(irisid3) <- c("RunName", "Accuracy", "Precision", "Recall", "Fscore")
colnames(irisknn) <- c("RunName", "Accuracy", "Precision", "Recall", "Fscore")
colnames(iristan) <- c("RunName", "Accuracy", "Precision", "Recall", "Fscore")

colnames(irisnbtrain) <- c("RunName", "Accuracy", "Precision", "Recall", "Fscore")
colnames(irisid3train) <- c("RunName", "Accuracy", "Precision", "Recall", "Fscore")
colnames(iristantrain) <- c("RunName", "Accuracy", "Precision", "Recall", "Fscore")

colnames(soynb) <- c("RunName", "Accuracy", "Precision", "Recall", "Fscore")
colnames(soyid3) <- c("RunName", "Accuracy", "Precision", "Recall", "Fscore")
colnames(soyknn) <- c("RunName", "Accuracy", "Precision", "Recall", "Fscore")
colnames(soytan) <- c("RunName", "Accuracy", "Precision", "Recall", "Fscore")

colnames(soynbtrain) <- c("RunName", "Accuracy", "Precision", "Recall", "Fscore")
colnames(soyid3train) <- c("RunName", "Accuracy", "Precision", "Recall", "Fscore")
colnames(soytantrain) <- c("RunName", "Accuracy", "Precision", "Recall", "Fscore")

#########################
# Test imputation

t.test(houseknnT$Accuracy, houseknnF$Accuracy, var.equal = FALSE)
t.test(houseknnT$Precision, houseknnF$Precision, var.equal = FALSE)
t.test(houseknnT$Recall, houseknnF$Recall, var.equal = FALSE)
t.test(houseknnT$Fscore, houseknnF$Fscore, var.equal = FALSE)

t.test(housenbT$Accuracy, housenbF$Accuracy, var.equal = FALSE)
t.test(housenbT$Precision, housenbF$Precision, var.equal = FALSE)
t.test(housenbT$Recall, housenbF$Recall, var.equal = FALSE)
t.test(housenbT$Fscore, housenbF$Fscore, var.equal = FALSE)

t.test(housetanT$Accuracy, housetanF$Accuracy, var.equal = FALSE)
t.test(housetanT$Precision, housetanF$Precision, var.equal = FALSE)
t.test(housetanT$Recall, housetanF$Recall, var.equal = FALSE)
t.test(housetanT$Fscore, housetanF$Fscore, var.equal = FALSE)

t.test(houseid3T$Accuracy, houseid3F$Accuracy, var.equal = FALSE)
t.test(houseid3T$Precision, houseid3F$Precision, var.equal = FALSE)
t.test(houseid3T$Recall, houseid3F$Recall, var.equal = FALSE)
t.test(houseid3T$Fscore, houseid3F$Fscore, var.equal = FALSE)



t.test(breastknnT$Accuracy, breastknnF$Accuracy, var.equal = FALSE)
t.test(breastknnT$Precision, breastknnF$Precision, var.equal = FALSE)
t.test(breastknnT$Recall, breastknnF$Recall, var.equal = FALSE)
t.test(breastknnT$Fscore, breastknnF$Fscore, var.equal = FALSE)

t.test(breastnbT$Accuracy, breastnbF$Accuracy, var.equal = FALSE)
t.test(breastnbT$Precision, breastnbF$Precision, var.equal = FALSE)
t.test(breastnbT$Recall, breastnbF$Recall, var.equal = FALSE)
t.test(breastnbT$Fscore, breastnbF$Fscore, var.equal = FALSE)

t.test(breasttanT$Accuracy, breasttanF$Accuracy, var.equal = FALSE)
t.test(breasttanT$Precision, breasttanF$Precision, var.equal = FALSE)
t.test(breasttanT$Recall, breasttanF$Recall, var.equal = FALSE)
t.test(breasttanT$Fscore, breasttanF$Fscore, var.equal = FALSE)

t.test(breastid3T$Accuracy, breastid3F$Accuracy, var.equal = FALSE)
t.test(breastid3T$Precision, breastid3F$Precision, var.equal = FALSE)
t.test(breastid3T$Recall, breastid3F$Recall, var.equal = FALSE)
t.test(breastid3T$Fscore, breastid3F$Fscore, var.equal = FALSE)

######################
#Test Training Error

t.test(breastnbT$Accuracy, breastnbTtrain$Accuracy, var.equal = FALSE)
t.test(breastid3T$Accuracy, breastid3Ttrain$Accuracy, var.equal = FALSE)
t.test(breasttanT$Accuracy, breasttanTtrain$Accuracy, var.equal = FALSE)

t.test(breastnbF$Accuracy, breastnbFtrain$Accuracy, var.equal = FALSE)
t.test(breastid3F$Accuracy, breastid3Ftrain$Accuracy, var.equal = FALSE)
t.test(breasttanF$Accuracy, breasttanFtrain$Accuracy, var.equal = FALSE)

t.test(housenbT$Accuracy, housenbTtrain$Accuracy, var.equal = FALSE)
t.test(houseid3T$Accuracy, houseid3Ttrain$Accuracy, var.equal = FALSE)
t.test(housetanT$Accuracy, housetanTtrain$Accuracy, var.equal = FALSE)

t.test(housenbF$Accuracy, housenbFtrain$Accuracy, var.equal = FALSE)
t.test(houseid3F$Accuracy, houseid3Ftrain$Accuracy, var.equal = FALSE)
t.test(housetanF$Accuracy, housetanFtrain$Accuracy, var.equal = FALSE)

t.test(irisnb$Accuracy, irisnbtrain$Accuracy, var.equal = FALSE)
t.test(irisid3$Accuracy, irisid3train$Accuracy, var.equal = FALSE)
t.test(iristan$Accuracy, iristantrain$Accuracy, var.equal = FALSE)

t.test(glassnb$Accuracy, glassnbtrain$Accuracy, var.equal = FALSE)
t.test(glassid3$Accuracy, glassid3train$Accuracy, var.equal = FALSE)
t.test(glasstan$Accuracy, glasstantrain$Accuracy, var.equal = FALSE)

t.test(soynb$Accuracy, soynbtrain$Accuracy, var.equal = FALSE)
t.test(soyid3$Accuracy, soyid3train$Accuracy, var.equal = FALSE)
t.test(soytan$Accuracy, soytantrain$Accuracy, var.equal = FALSE)

######################
#Test Algorithms

#House not imputed
t.test(housenbF$Accuracy, houseid3F$Accuracy, var.equal = FALSE)
t.test(housetanF$Accuracy, houseid3F$Accuracy, var.equal = FALSE)
t.test(housetanF$Accuracy, houseknnF$Accuracy, var.equal = FALSE)
t.test(housenbF$Accuracy, houseknnF$Accuracy, var.equal = FALSE)
t.test(housetanF$Accuracy, housenbF$Accuracy, var.equal = FALSE)
t.test(houseknnF$Accuracy, houseid3F$Accuracy, var.equal = FALSE)

t.test(housenbF$Precision, houseid3F$Precision, var.equal = FALSE)
t.test(housetanF$Precision, houseid3F$Precision, var.equal = FALSE)
t.test(housetanF$Precision, houseknnF$Precision, var.equal = FALSE)
t.test(housenbF$Precision, houseknnF$Precision, var.equal = FALSE)
t.test(housetanF$Precision, housenbF$Precision, var.equal = FALSE)
t.test(houseknnF$Precision, houseid3F$Precision, var.equal = FALSE)

t.test(housenbF$Recall, houseid3F$Recall, var.equal = FALSE)
t.test(housetanF$Recall, houseid3F$Recall, var.equal = FALSE)
t.test(housetanF$Recall, houseknnF$Recall, var.equal = FALSE)
t.test(housenbF$Recall, houseknnF$Recall, var.equal = FALSE)
t.test(housetanF$Recall, housenbF$Recall, var.equal = FALSE)
t.test(houseknnF$Recall, houseid3F$Recall, var.equal = FALSE)

t.test(housenbF$Fscore, houseid3F$Fscore, var.equal = FALSE)
t.test(housetanF$Fscore, houseid3F$Fscore, var.equal = FALSE)
t.test(housetanF$Fscore, houseknnF$Fscore, var.equal = FALSE)
t.test(housenbF$Fscore, houseknnF$Fscore, var.equal = FALSE)
t.test(housetanF$Fscore, housenbF$Fscore, var.equal = FALSE)
t.test(houseknnF$Fscore, houseid3F$Fscore, var.equal = FALSE)

#House imputed
t.test(housenbT$Accuracy, houseid3T$Accuracy, var.equal = FALSE)
t.test(housetanT$Accuracy, houseid3T$Accuracy, var.equal = FALSE)
t.test(housetanT$Accuracy, houseknnT$Accuracy, var.equal = FALSE)
t.test(housenbT$Accuracy, houseknnT$Accuracy, var.equal = FALSE)
t.test(housetanT$Accuracy, housenbT$Accuracy, var.equal = FALSE)
t.test(houseknnT$Accuracy, houseid3T$Accuracy, var.equal = FALSE)

t.test(housenbT$Precision, houseid3T$Precision, var.equal = FALSE)
t.test(housetanT$Precision, houseid3T$Precision, var.equal = FALSE)
t.test(housetanT$Precision, houseknnT$Precision, var.equal = FALSE)
t.test(housenbT$Precision, houseknnT$Precision, var.equal = FALSE)
t.test(housetanT$Precision, housenbT$Precision, var.equal = FALSE)
t.test(houseknnT$Precision, houseid3T$Precision, var.equal = FALSE)

t.test(housenbT$Recall, houseid3T$Recall, var.equal = FALSE)
t.test(housetanT$Recall, houseid3T$Recall, var.equal = FALSE)
t.test(housetanT$Recall, houseknnT$Recall, var.equal = FALSE)
t.test(housenbT$Recall, houseknnT$Recall, var.equal = FALSE)
t.test(housetanT$Recall, housenbT$Recall, var.equal = FALSE)
t.test(houseknnT$Recall, houseid3T$Recall, var.equal = FALSE)

t.test(housenbT$Fscore, houseid3T$Fscore, var.equal = FALSE)
t.test(housetanT$Fscore, houseid3T$Fscore, var.equal = FALSE)
t.test(housetanT$Fscore, houseknnT$Fscore, var.equal = FALSE)
t.test(housenbT$Fscore, houseknnT$Fscore, var.equal = FALSE)
t.test(housetanT$Fscore, housenbT$Fscore, var.equal = FALSE)
t.test(houseknnT$Fscore, houseid3T$Fscore, var.equal = FALSE)

#Breast not imputed
t.test(breastnbF$Accuracy, breastid3F$Accuracy, var.equal = FALSE)
t.test(breasttanF$Accuracy, breastid3F$Accuracy, var.equal = FALSE)
t.test(breasttanF$Accuracy, breastknnF$Accuracy, var.equal = FALSE)
t.test(breastnbF$Accuracy, breastknnF$Accuracy, var.equal = FALSE)
t.test(breasttanF$Accuracy, breastnbF$Accuracy, var.equal = FALSE)
t.test(breastknnF$Accuracy, breastid3F$Accuracy, var.equal = FALSE)

t.test(breastnbF$Precision, breastid3F$Precision, var.equal = FALSE)
t.test(breasttanF$Precision, breastid3F$Precision, var.equal = FALSE)
t.test(breasttanF$Precision, breastknnF$Precision, var.equal = FALSE)
t.test(breastnbF$Precision, breastknnF$Precision, var.equal = FALSE)
t.test(breasttanF$Precision, breastnbF$Precision, var.equal = FALSE)
t.test(breastknnF$Precision, breastid3F$Precision, var.equal = FALSE)

t.test(breastnbF$Recall, breastid3F$Recall, var.equal = FALSE)
t.test(breasttanF$Recall, breastid3F$Recall, var.equal = FALSE)
t.test(breasttanF$Recall, breastknnF$Recall, var.equal = FALSE)
t.test(breastnbF$Recall, breastknnF$Recall, var.equal = FALSE)
t.test(breasttanF$Recall, breastnbF$Recall, var.equal = FALSE)
t.test(breastknnF$Recall, breastid3F$Recall, var.equal = FALSE)

t.test(breastnbF$Fscore, breastid3F$Fscore, var.equal = FALSE)
t.test(breasttanF$Fscore, breastid3F$Fscore, var.equal = FALSE)
t.test(breasttanF$Fscore, breastknnF$Fscore, var.equal = FALSE)
t.test(breastnbF$Fscore, breastknnF$Fscore, var.equal = FALSE)
t.test(breasttanF$Fscore, breastnbF$Fscore, var.equal = FALSE)
t.test(breastknnF$Fscore, breastid3F$Fscore, var.equal = FALSE)

#Breast imputed
t.test(breastnbT$Accuracy, breastid3T$Accuracy, var.equal = FALSE)
t.test(breasttanT$Accuracy, breastid3T$Accuracy, var.equal = FALSE)
t.test(breasttanT$Accuracy, breastknnT$Accuracy, var.equal = FALSE)
t.test(breastnbT$Accuracy, breastknnT$Accuracy, var.equal = FALSE)
t.test(breasttanT$Accuracy, breastnbT$Accuracy, var.equal = FALSE)
t.test(breastknnT$Accuracy, breastid3T$Accuracy, var.equal = FALSE)

t.test(breastnbT$Precision, breastid3T$Precision, var.equal = FALSE)
t.test(breasttanT$Precision, breastid3T$Precision, var.equal = FALSE)
t.test(breasttanT$Precision, breastknnT$Precision, var.equal = FALSE)
t.test(breastnbT$Precision, breastknnT$Precision, var.equal = FALSE)
t.test(breasttanT$Precision, breastnbT$Precision, var.equal = FALSE)
t.test(breastknnT$Precision, breastid3T$Precision, var.equal = FALSE)

t.test(breastnbT$Recall, breastid3T$Recall, var.equal = FALSE)
t.test(breasttanT$Recall, breastid3T$Recall, var.equal = FALSE)
t.test(breasttanT$Recall, breastknnT$Recall, var.equal = FALSE)
t.test(breastnbT$Recall, breastknnT$Recall, var.equal = FALSE)
t.test(breasttanT$Recall, breastnbT$Recall, var.equal = FALSE)
t.test(breastknnT$Recall, breastid3T$Recall, var.equal = FALSE)

t.test(breastnbT$Fscore, breastid3T$Fscore, var.equal = FALSE)
t.test(breasttanT$Fscore, breastid3T$Fscore, var.equal = FALSE)
t.test(breasttanT$Fscore, breastknnT$Fscore, var.equal = FALSE)
t.test(breastnbT$Fscore, breastknnT$Fscore, var.equal = FALSE)
t.test(breasttanT$Fscore, breastnbT$Fscore, var.equal = FALSE)
t.test(breastknnT$Fscore, breastid3T$Fscore, var.equal = FALSE)

#iris
t.test(irisnb$Accuracy, irisid3$Accuracy, var.equal = FALSE)
t.test(iristan$Accuracy, irisid3$Accuracy, var.equal = FALSE)
t.test(iristan$Accuracy, irisknn$Accuracy, var.equal = FALSE)
t.test(irisnb$Accuracy, irisknn$Accuracy, var.equal = FALSE)
t.test(iristan$Accuracy, irisnb$Accuracy, var.equal = FALSE)
t.test(irisknn$Accuracy, irisid3$Accuracy, var.equal = FALSE)

t.test(irisnb$Precision, irisid3$Precision, var.equal = FALSE)
t.test(iristan$Precision, irisid3$Precision, var.equal = FALSE)
t.test(iristan$Precision, irisknn$Precision, var.equal = FALSE)
t.test(irisnb$Precision, irisknn$Precision, var.equal = FALSE)
t.test(iristan$Precision, irisnb$Precision, var.equal = FALSE)
t.test(irisknn$Precision, irisid3$Precision, var.equal = FALSE)

t.test(irisnb$Recall, irisid3$Recall, var.equal = FALSE)
t.test(iristan$Recall, irisid3$Recall, var.equal = FALSE)
t.test(iristan$Recall, irisknn$Recall, var.equal = FALSE)
t.test(irisnb$Recall, irisknn$Recall, var.equal = FALSE)
t.test(iristan$Recall, irisnb$Recall, var.equal = FALSE)
t.test(irisknn$Recall, irisid3$Recall, var.equal = FALSE)

t.test(irisnb$Fscore, irisid3$Fscore, var.equal = FALSE)
t.test(iristan$Fscore, irisid3$Fscore, var.equal = FALSE)
t.test(iristan$Fscore, irisknn$Fscore, var.equal = FALSE)
t.test(irisnb$Fscore, irisknn$Fscore, var.equal = FALSE)
t.test(iristan$Fscore, irisnb$Fscore, var.equal = FALSE)
t.test(irisknn$Fscore, irisid3$Fscore, var.equal = FALSE)


#glass
t.test(glassnb$Accuracy, glassid3$Accuracy, var.equal = FALSE)
t.test(glasstan$Accuracy, glassid3$Accuracy, var.equal = FALSE)
t.test(glasstan$Accuracy, glassknn$Accuracy, var.equal = FALSE)
t.test(glassnb$Accuracy, glassknn$Accuracy, var.equal = FALSE)
t.test(glasstan$Accuracy, glassnb$Accuracy, var.equal = FALSE)
t.test(glassknn$Accuracy, glassid3$Accuracy, var.equal = FALSE)

t.test(glassnb$Precision, glassid3$Precision, var.equal = FALSE)
t.test(glasstan$Precision, glassid3$Precision, var.equal = FALSE)
t.test(glasstan$Precision, glassknn$Precision, var.equal = FALSE)
t.test(glassnb$Precision, glassknn$Precision, var.equal = FALSE)
t.test(glasstan$Precision, glassnb$Precision, var.equal = FALSE)
t.test(glassknn$Precision, glassid3$Precision, var.equal = FALSE)

t.test(glassnb$Recall, glassid3$Recall, var.equal = FALSE)
t.test(glasstan$Recall, glassid3$Recall, var.equal = FALSE)
t.test(glasstan$Recall, glassknn$Recall, var.equal = FALSE)
t.test(glassnb$Recall, glassknn$Recall, var.equal = FALSE)
t.test(glasstan$Recall, glassnb$Recall, var.equal = FALSE)
t.test(glassknn$Recall, glassid3$Recall, var.equal = FALSE)

t.test(glassnb$Fscore, glassid3$Fscore, var.equal = FALSE)
t.test(glasstan$Fscore, glassid3$Fscore, var.equal = FALSE)
t.test(glasstan$Fscore, glassknn$Fscore, var.equal = FALSE)
t.test(glassnb$Fscore, glassknn$Fscore, var.equal = FALSE)
t.test(glasstan$Fscore, glassnb$Fscore, var.equal = FALSE)
t.test(glassknn$Fscore, glassid3$Fscore, var.equal = FALSE)

#soy
t.test(soynb$Accuracy, soyid3$Accuracy, var.equal = FALSE)
t.test(soytan$Accuracy, soyid3$Accuracy, var.equal = FALSE)
t.test(soytan$Accuracy, soyknn$Accuracy, var.equal = FALSE)
t.test(soynb$Accuracy, soyknn$Accuracy, var.equal = FALSE)
t.test(soytan$Accuracy, soynb$Accuracy, var.equal = FALSE)
t.test(soyknn$Accuracy, soyid3$Accuracy, var.equal = FALSE)

t.test(soynb$Precision, soyid3$Precision, var.equal = FALSE)
t.test(soytan$Precision, soyid3$Precision, var.equal = FALSE)
t.test(soytan$Precision, soyknn$Precision, var.equal = FALSE)
t.test(soynb$Precision, soyknn$Precision, var.equal = FALSE)
t.test(soytan$Precision, soynb$Precision, var.equal = FALSE)
t.test(soyknn$Precision, soyid3$Precision, var.equal = FALSE)

t.test(soynb$Recall, soyid3$Recall, var.equal = FALSE)
t.test(soytan$Recall, soyid3$Recall, var.equal = FALSE)
t.test(soytan$Recall, soyknn$Recall, var.equal = FALSE)
t.test(soynb$Recall, soyknn$Recall, var.equal = FALSE)
t.test(soytan$Recall, soynb$Recall, var.equal = FALSE)
t.test(soyknn$Recall, soyid3$Recall, var.equal = FALSE)

t.test(soynb$Fscore, soyid3$Fscore, var.equal = FALSE)
t.test(soytan$Fscore, soyid3$Fscore, var.equal = FALSE)
t.test(soytan$Fscore, soyknn$Fscore, var.equal = FALSE)
t.test(soynb$Fscore, soyknn$Fscore, var.equal = FALSE)
t.test(soytan$Fscore, soynb$Fscore, var.equal = FALSE)
t.test(soyknn$Fscore, soyid3$Fscore, var.equal = FALSE)

