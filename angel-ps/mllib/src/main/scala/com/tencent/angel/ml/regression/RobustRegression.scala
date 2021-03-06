/*
 * Tencent is pleased to support the open source community by making Angel available.
 *
 * Copyright (C) 2017-2018 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in 
 * compliance with the License. You may obtain a copy of the License at
 *
 * https://opensource.org/licenses/Apache-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 */


package com.tencent.angel.ml.regression

import com.tencent.angel.ml.core.PSOptimizerProvider
import com.tencent.angel.mlcore.conf.{MLCoreConf, SharedConf}
import com.tencent.angel.ml.core.conf.AngelMLConf
import com.tencent.angel.ml.core.graphsubmit.AngelModel
import com.tencent.angel.mlcore.network.Identity
import com.tencent.angel.mlcore.network.layers.LossLayer
import com.tencent.angel.mlcore.network.layers.leaf.SimpleInputLayer
import com.tencent.angel.mlcore.optimizer.loss.HuberLoss
import com.tencent.angel.worker.task.TaskContext


class RobustRegression(conf: SharedConf, _ctx: TaskContext = null) extends AngelModel(conf, _ctx) {
  val optProvider = new PSOptimizerProvider(conf)

  override def buildNetwork(): this.type = {
    val ipOptName: String = conf.get(MLCoreConf.ML_INPUTLAYER_OPTIMIZER, MLCoreConf.DEFAULT_ML_INPUTLAYER_OPTIMIZER)

    val input = new SimpleInputLayer("input", 1, new Identity(), optProvider.getOptimizer(ipOptName))

    new LossLayer("simpleLossLayer", input,
      new HuberLoss(conf.getDouble(AngelMLConf.ML_ROBUSTREGRESSION_LOSS_DELTA, 1.0)))

    this
  }

}